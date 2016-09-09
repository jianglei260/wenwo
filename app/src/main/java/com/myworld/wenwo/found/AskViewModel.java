package com.myworld.wenwo.found;

import android.app.Fragment;
import android.content.Context;
import android.databinding.ListChangeRegistry;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.mob.tools.gui.PullToRequestBaseAdapter;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.adapter.QuickRecyclerAdapterFactory;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.common.viewmodel.ListItemViewModel;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.data.source.local.AskLocalDataSource;
import com.myworld.wenwo.data.source.remote.AskRemoteDataSource;
import com.myworld.wenwo.like.LikeFragment;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.utils.RealmUtil;

/**
 * Created by jianglei on 16/7/16.
 */

public class AskViewModel implements ViewModel, LikeStateChangedListener {
    private Context context;
    private int currentPage;
    private boolean loadMoreEnable = true;
    private long mainId;
    private AskDataInterface dataInterface;
    private RecyclerView recyclerView;
    public final ItemViewSelector<ListItemViewModel> itemView = new ItemViewSelector<ListItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, ListItemViewModel item) {
            switch (item.viewType.get()) {
                case ListItemViewModel.VIEW_TYPE_NORMAL:
                    AskItemViewModel itemViewModel = (AskItemViewModel) item;
                    if (TextUtils.isEmpty(itemViewModel.bigImage.get()))
                        itemView.set(BR.viewModel, R.layout.list_ask_item_small);
                    else
                        itemView.set(BR.viewModel, R.layout.list_ask_item_big);
                    break;
                case ListItemViewModel.VIEW_TYPE_LOAD_MORE:
                    itemView.set(BR.viewModel, R.layout.list_load_more_item);
                    break;
                case ListItemViewModel.VIEW_TYPE_LOAD_FINISH:
                    itemView.set(BR.viewModel, R.layout.list_load_finish_item);
                    break;
                case ListItemViewModel.VIEW_TYPE_BANNER:
                    itemView.set(BR.bannerViewModel, R.layout.list_banner);
                    break;
                case ListItemViewModel.VIEW_TYPE_NAVIGATION:
                    itemView.set(BR.navigationViewModel, R.layout.list_navigation);
                    break;
                case ListItemViewModel.VIEW_TYPE_LOCATION:
                    itemView.set(BR.viewModel, R.layout.list_location);
                    break;
            }
        }

        @Override
        public int viewTypeCount() {
            return 5;
        }
    };
    public ObservableBoolean isRefresh = new ObservableBoolean(true);
    public ReplyCommand<Integer> loadMoreCommand = new ReplyCommand<Integer>(new Action1<Integer>() {
        @Override
        public void call(Integer number) {
            if (!loadMoreEnable)
                return;
            loadDatas(false);
        }
    });
    public ReplyCommand refreshCommand = new ReplyCommand(new Action0() {
        @Override
        public void call() {
            isRefresh.set(true);
            currentPage = 0;
            loadDatas(true);
        }
    });

    public void removeItem(String askId) {
        for (ListItemViewModel itemViewModel : itemViewModels) {
            if (itemViewModel.viewType.get() == ListItemViewModel.VIEW_TYPE_NORMAL) {
                AskItemViewModel askItemViewModel = (AskItemViewModel) itemViewModel;
                if (askItemViewModel.askMe.getObjectId().equals(askId)) {
                    itemViewModels.remove(itemViewModel);
                }
            }
        }
    }

    public void refresh() {
        if (!isRefresh.get()) {
            isRefresh.set(true);
            currentPage = 0;
            loadDatas(true);
        }
    }

    private void loadDatas(final boolean refresh) {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<AskMe>>() {
            @Override
            public void call(Subscriber<? super List<AskMe>> subscriber) {
                RealmUtil.ensureMainThread(mainId);
                if (refresh) {
                    isRefresh.set(true);
                    currentPage = 0;
                    List<AskMe> result = dataInterface.refresh();
                    subscriber.onNext(result);
                } else {
                    List<AskMe> result = dataInterface.dataSet(currentPage);
                    subscriber.onNext(result);
                }
            }
        }, new Subscriber<List<AskMe>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<AskMe> askMes) {
                if (refresh) {
                    ListIterator<ListItemViewModel> iterator = itemViewModels.listIterator();
                    while (iterator.hasNext()) {
                        ListItemViewModel itemViewModel = iterator.next();
                        if (itemViewModel.viewType.get() != ListItemViewModel.VIEW_TYPE_BANNER && itemViewModel.viewType.get() != ListItemViewModel.VIEW_TYPE_NAVIGATION && itemViewModel.viewType.get() != ListItemViewModel.VIEW_TYPE_LOCATION)
                            iterator.remove();
                    }
                }
                if (currentPage == 0) {
                    ListItemViewModel askItemViewModel = new ListItemViewModel();
                    askItemViewModel.viewType.set(ListItemViewModel.VIEW_TYPE_LOAD_MORE);
                    itemViewModels.add(askItemViewModel);
                }
                if (askMes.size() < 20 || askMes.size() > 20) {
                    loadMoreEnable = false;
                    itemViewModels.get(itemViewModels.size() - 1).viewType.set(ListItemViewModel.VIEW_TYPE_LOAD_FINISH);
                } else {
                    loadMoreEnable = true;
                }
                for (int i = 0; i < askMes.size(); i++) {
                    Log.d("______render item", askMes.get(i).toString());
                    AskItemViewModel itemViewModel = new AskItemViewModel(context, askMes.get(i), itemViewModels.size() - 1);
                    itemViewModel.viewType.set(ListItemViewModel.VIEW_TYPE_NORMAL);
                    itemViewModel.setLikeStateChangedListener(AskViewModel.this);
                    itemViewModel.setStateChangeListener(stateChangeListener);
                    itemViewModels.add(itemViewModels.size() - 1, itemViewModel);
                }
                if (askMes != null && askMes.size() > 0)
                    currentPage++;
                isRefresh.set(false);
            }
        });
    }

    private AskStateChangeListener stateChangeListener;

    public void setStateChangeListener(AskStateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public volatile ObservableList<ListItemViewModel> itemViewModels = new ObservableArrayList<>();
    public QuickRecyclerAdapterFactory factory = new QuickRecyclerAdapterFactory();

    public AskViewModel(Context context, AskDataInterface dataInterface) {
        this.context = context;
        this.dataInterface = dataInterface;
        loadDatas(false);
        mainId = Thread.currentThread().getId();

    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void itemChanged(int position) {
        if (position >= 0) {
            itemViewModels.get(position).dataChanged();
        }
    }

    public void itemRemoved(int position) {
        if (position >= 0) {
            itemViewModels.remove(position);
        }
    }

    public void addItemViewModel(ListItemViewModel itemViewModel, int position) {
        itemViewModels.add(position, itemViewModel);
    }

    public LikeStateChangedListener likeStateChangedListener;

    public LikeStateChangedListener getLikeStateChangedListener() {
        return likeStateChangedListener;
    }

    public void setLikeStateChangedListener(LikeStateChangedListener likeStateChangedListener) {
        this.likeStateChangedListener = likeStateChangedListener;
    }

    @Override
    public void liked(int position) {
        itemChanged(position);
        if (recyclerView != null)
            recyclerView.getAdapter().notifyItemChanged(position);
        if (likeStateChangedListener != null)
            likeStateChangedListener.liked(position);

    }

    public AskStateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    @Override
    public void disLiked(int position) {
        itemChanged(position);
        if (recyclerView != null)
            recyclerView.getAdapter().notifyItemChanged(position);
        if (likeStateChangedListener != null)
            likeStateChangedListener.disLiked(position);
    }

    public interface AskDataInterface {
        public List<AskMe> dataSet(int page);

        public List<AskMe> refresh();

        public void remove(AskMe askMe);
    }

    private static final String TAG = "AskViewModel";
}
