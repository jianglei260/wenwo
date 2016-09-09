package com.myworld.wenwo.card;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.CardItemLayoutBinding;
import com.myworld.wenwo.utils.BitmapUtil;
import com.myworld.wenwo.utils.ObservableUtil;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;


/**
 * Created by jianglei on 16/8/29.
 */

public class CardViewModel implements ViewModel {
    private Context context;
    private int index = 0;
    public ObservableBoolean isRefresh = new ObservableBoolean(true);
    public ObservableList<CardItemViewModel> items = new ObservableArrayList<>();
    public ObservableField<String> likeNum = new ObservableField<>();
    public ObservableField<String> downNum = new ObservableField<>();
    public ObservableBoolean liked = new ObservableBoolean();
    public ObservableBoolean visible = new ObservableBoolean(true);

    public ItemViewSelector<CardItemViewModel> itemView = new ItemViewSelector<CardItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, CardItemViewModel item) {
            itemView.set(BR.viewModel, R.layout.card_item_layout);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };
    public ReplyCommand<Integer> cardSelcted = new ReplyCommand<Integer>(new Action1<Integer>() {
        @Override
        public void call(Integer integer) {
            index = integer;
            final CardItemViewModel itemViewModel = items.get(integer);
            itemViewModel.liked.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    liked.set(itemViewModel.liked.get());
                }
            });
            itemViewModel.likeNum.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    likeNum.set("" + itemViewModel.likeNum.get());
                }
            });
            itemViewModel.downNum.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    downNum.set("" + itemViewModel.downNum.get());
                }
            });
            liked.set(itemViewModel.liked.get());
            likeNum.set("" + itemViewModel.likeNum.get());
            downNum.set("" + itemViewModel.downNum.get());
        }
    });

    public ReplyCommand lookClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            int index = cardStack.getCurrIndex();
            items.get(index).lookClick.execute();
        }
    });

    public ReplyCommand likeClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            int index = cardStack.getCurrIndex();
            items.get(index).likeClick.execute();
        }
    });

    public ReplyCommand shareClick = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            int index = cardStack.getCurrIndex();
            shareCard(items.get(index), ((ViewGroup) cardStack.getTopView()));
        }
    });
    private CardStack cardStack;


    private void shareCard(final CardItemViewModel itemViewModel, final View v) {
        itemViewModel.sloganVisibility.set(true);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.dp4);
        v.setPadding(padding, padding, padding, padding);
        v.postInvalidate();
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        ObservableUtil.runOnUI(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                AskMeRepository.getInstance().addCardDownNum(itemViewModel.cardId.get());
                subscriber.onNext(BitmapUtil.saveViewToStorage(v));
            }
        }, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String s) {
                dialog.dismiss();
                v.setPadding(0, 0, 0, 0);
                itemViewModel.sloganVisibility.set(false);
                downNum.set(Integer.valueOf(downNum.get()) + 1 + "");
                ShareSDK.initSDK(context);
                OnekeyShare oks = new OnekeyShare();
//                oks.setTitle(context.getString(R.string.card_share));
                oks.setImagePath(s);
                oks.setSilent(true);
                oks.setCustomerLogo(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_save), context.getString(R.string.save_to_local), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, context.getString(R.string.saved) + s, Toast.LENGTH_SHORT).show();
                    }
                });
                oks.setCustomerLogo(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_more_png), context.getString(R.string.more), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(s));
                        context.startActivity(Intent.createChooser(intent, context.getString(R.string.card_share)));
                    }
                });
                oks.show(context);
            }
        });
    }

    public CardViewModel(Context context) {
        this.context = context;
        cardStack = ((CardActivity) context).getCardStack();
        cardStack.setAdapter(new StackAdapter(items));
        cardStack.setListener(new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int section, float distance) {

                return true;
            }

            @Override
            public boolean swipeStart(int section, float distance) {
                return true;
            }

            @Override
            public boolean swipeContinue(int section, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void discarded(int mIndex, int direction) {
                if (cardStack.getCurrIndex() == cardStack.getAdapter().getCount() - 1) {
                    cardStack.setCanSwipe(false);
                    visible.set(false);
                } else {
                    cardStack.setCanSwipe(true);
                }
                if (mIndex < items.size())
                    cardSelcted.execute(mIndex);
            }

            @Override
            public void topCardTapped() {

            }
        });
        findCards();
    }

    public void findCards() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<CardItemViewModel>>() {
            @Override
            public void call(Subscriber<? super List<CardItemViewModel>> subscriber) {
                String result = AskMeRepository.getInstance().getCards(Config.USERNAME);
                List<CardItemViewModel> itemViewModels = new ArrayList<CardItemViewModel>();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") != 200)
                        return;
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cardObject = array.getJSONObject(i);
                        CardItemViewModel itemViewModel = new CardItemViewModel(context);
                        itemViewModel.imageUrl.set(cardObject.getString("cardImg"));
                        itemViewModel.descText.set(cardObject.getString("detail"));
                        itemViewModel.author.set("--" + cardObject.getString("byName"));
                        itemViewModel.objectId.set(cardObject.getString("askId"));
                        itemViewModel.likeNum.set(cardObject.getInt("likeNum"));
                        itemViewModel.downNum.set(cardObject.getInt("downNum"));
                        itemViewModel.liked.set(cardObject.getInt("liked") > 0);
                        itemViewModel.cardId.set(cardObject.getString("objectId"));
                        itemViewModels.add(itemViewModel);
                    }
                    subscriber.onNext(itemViewModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Subscriber<List<CardItemViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<CardItemViewModel> cardItemViewModels) {
                isRefresh.set(false);
                items.clear();
                items.addAll(cardItemViewModels);
                items.add(new CardItemViewModel(context));
                cardSelcted.execute(0);
            }
        });
    }

    class StackAdapter extends BaseAdapter {
        ObservableList<CardItemViewModel> items;

        public StackAdapter(ObservableList<CardItemViewModel> items) {
            this.items = items;
            this.items.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<CardItemViewModel>>() {
                @Override
                public void onChanged(ObservableList<CardItemViewModel> cardItemViewModels) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(ObservableList<CardItemViewModel> cardItemViewModels, int i, int i1) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeInserted(ObservableList<CardItemViewModel> cardItemViewModels, int i, int i1) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeMoved(ObservableList<CardItemViewModel> cardItemViewModels, int i, int i1, int i2) {
                    notifyDataSetChanged();
                }

                @Override
                public void onItemRangeRemoved(ObservableList<CardItemViewModel> cardItemViewModels, int i, int i1) {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == items.size() - 1) {
                View bottomView = LayoutInflater.from(context).inflate(R.layout.card_bottom_layout, cardStack, false);
                bottomView.findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cardStack.reset(true);
                        cardStack.setCanSwipe(true);
                        cardSelcted.execute(0);
                        visible.set(true);
                    }
                });
                return bottomView;
            }
            CardItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_item_layout, parent, false);
            binding.setVariable(BR.viewModel, getItem(position));
            binding.executePendingBindings();
            return binding.getRoot();
        }
    }
}
