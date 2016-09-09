package com.myworld.wenwo.add.edit;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myworld.wenwo.R;
import com.myworld.wenwo.add.AddActivity;
import com.myworld.wenwo.add.AddressItemViewModel;
import com.myworld.wenwo.add.DetailItemViewModel;
import com.myworld.wenwo.add.PriceItemViewModel;
import com.myworld.wenwo.add.ReasonItemViewModel;
import com.myworld.wenwo.add.ShopItemViewModel;
import com.myworld.wenwo.add.TagItemViewModel;
import com.myworld.wenwo.add.preview.PreviewViewModel;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.entity.DetialTag;
import com.myworld.wenwo.databinding.AddAddressItemBinding;
import com.myworld.wenwo.databinding.AddDetailItemBinding;
import com.myworld.wenwo.databinding.AddPriceItemBinding;
import com.myworld.wenwo.databinding.AddReasonItemBinding;
import com.myworld.wenwo.databinding.AddShopItemBinding;
import com.myworld.wenwo.databinding.AddTagItemBinding;
import com.myworld.wenwo.detial.DetialTagViewModel;
import com.myworld.wenwo.view.widget.IndicatorItem;
import com.myworld.wenwo.view.widget.MapViewWrapper;
import com.myworld.wenwo.view.widget.ScrollbleViewPager;
import com.myworld.wenwo.view.widget.TitleBar;
import com.myworld.wenwo.view.widget.ViewPagerIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jianglei on 16/7/21.
 */

public class EditFragment extends Fragment {
    private ScrollbleViewPager viewPager;
    private AddTagItemBinding addTagItemBinding;
    private AddReasonItemBinding addReasonItemBinding;
    private AddDetailItemBinding addDetailItemBinding;
    private AddShopItemBinding addShopItemBinding;
    private AddAddressItemBinding addAddressItemBinding;
    private AddPriceItemBinding addPriceItemBinding;

    public static EditFragment newInstance() {

        Bundle args = new Bundle();

        EditFragment fragment = new EditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        viewPager = (ScrollbleViewPager) root.findViewById(R.id.view_pager);
        ViewPagerIndicator indicator = (ViewPagerIndicator) root.findViewById(R.id.indicator);
        initDataBinding();
        View[] views = new View[]{addTagItemBinding.getRoot(), addReasonItemBinding.getRoot(), addDetailItemBinding.getRoot(), addShopItemBinding.getRoot()
                , addAddressItemBinding.getRoot(), addPriceItemBinding.getRoot()};
        ViewPagerAdapter adapter = new ViewPagerAdapter(views);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 4) {
                    viewPager.setScrollble(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("state", state + "");
                if (ViewPager.SCROLL_STATE_IDLE == state && viewPager.getCurrentItem() == 4) {
                    viewPager.setScrollble(false);
                }

            }
        });
        viewPager.setAdapter(adapter);
        indicator.addItem(new IndicatorItem(R.drawable.ic_tag, "标签", Color.parseColor("#2B82FD")))
                .addItem(new IndicatorItem(R.drawable.ic_suggest, "理由", Color.parseColor("#F13A01")))
                .addItem(new IndicatorItem(R.drawable.ic_detial, "详情", Color.parseColor("#F4BC1C")))
                .addItem(new IndicatorItem(R.drawable.ic_shop, "商店", Color.parseColor("#5459CD")))
                .addItem(new IndicatorItem(R.drawable.ic_address, "地址", Color.parseColor("#42B93A")))
                .addItem(new IndicatorItem(R.drawable.ic_price, "价格", Color.parseColor("#F88827")))
                .initialise();
        indicator.setViewPager(viewPager);
        TitleBar titleBar = (TitleBar) root.findViewById(R.id.title_bar);
        titleBar.getLeftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddActivity) getActivity()).onBackPressed();
            }
        });
        titleBar.getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddActivity) getActivity()).onBackPressed();
            }
        });
        return root;
    }

    public void showPage(int index) {
        viewPager.setCurrentItem(index);
    }

    private TagItemViewModel tagItemViewModel;
    private ReasonItemViewModel reasonItemViewModel;
    private DetailItemViewModel detailItemViewModel;
    private ShopItemViewModel shopItemViewModel;
    private AddressItemViewModel addressItemViewModel;
    private PriceItemViewModel priceItemViewModel;
    private MapViewWrapper wrapper;

    private void initDataBinding() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        addTagItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_tag_item, viewPager, false);
        addReasonItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_reason_item, viewPager, false);
        addDetailItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_detail_item, viewPager, false);
        addShopItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_shop_item, viewPager, false);
        addAddressItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_address_item, viewPager, false);
        addPriceItemBinding = DataBindingUtil.inflate(inflater, R.layout.add_price_item, viewPager, false);

        tagItemViewModel = new TagItemViewModel(getActivity());
        reasonItemViewModel = new ReasonItemViewModel(getActivity());
        detailItemViewModel = new DetailItemViewModel(getActivity());
        shopItemViewModel = new ShopItemViewModel(getActivity());
        addressItemViewModel = new AddressItemViewModel(getActivity());
        priceItemViewModel = new PriceItemViewModel(getActivity());

        addTagItemBinding.setTagItemModel(tagItemViewModel);
        addReasonItemBinding.setReasonItemModel(reasonItemViewModel);
        addDetailItemBinding.setDetailViewModel(detailItemViewModel);
        addShopItemBinding.setShopItemModel(shopItemViewModel);
        addAddressItemBinding.setAddressItemModel(addressItemViewModel);
        addPriceItemBinding.setPriceItemModel(priceItemViewModel);
        setObserblePreview(((AddActivity) getActivity()).getPreviewViewModel());
        wrapper = (MapViewWrapper) addAddressItemBinding.getRoot().findViewById(R.id.map_view_wrapper);
        getMapViewWrapper().setListener(new MapViewWrapper.AddressChangedListener() {
            @Override
            public void onChanged() {
                ((AddActivity) getActivity()).getPreviewViewModel().address.set(getMapViewWrapper().getSimpleAddress());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void initViewModel(AskMe askMe) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONArray tags = new JSONArray(askMe.getAskTagStr());
            for (int i = 0; i < tags.length(); i++) {
                String tag = tags.getJSONObject(i).getString("tag_name");
                tagItemViewModel.content.add(tag);
                builder.append(tag + ";");
            }
            tagItemViewModel.contentString.set(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        reasonItemViewModel.content.set(askMe.getAskReason());

        String detialJson = askMe.getAskContentShow();
        try {
            JSONObject detialObject = new JSONObject(detialJson);
            detailItemViewModel.content.set(detialObject.getString("detail"));
            JSONArray tagArray = new JSONArray(detialObject.getString("detailLi"));

            for (int i = 0; i < tagArray.length(); i++) {
                JSONObject tagObject = tagArray.getJSONObject(i);
                DetialTag detialTag = new DetialTag();
                detialTag.setName(tagObject.getString("name"));
                detialTag.setVal(tagObject.getString("val"));
                DetialTagViewModel detialTagViewModel = new DetialTagViewModel(getActivity(), detialTag);
                detialTagViewModel.setDetailItemViewModel(detailItemViewModel);
                detialTagViewModel.visible.set(true);
                detialTagViewModel.editable.set(true);
                detailItemViewModel.tags.add(detailItemViewModel.tags.size() - 1, detialTagViewModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        shopItemViewModel.content.set(askMe.getShopName());
        if (!TextUtils.isEmpty(askMe.getAskPosition()))
            setAddressDetail(askMe);
        priceItemViewModel.content.set(String.valueOf(askMe.getAskPrice()));
    }

    private void setAddressDetail(AskMe askMe) {
        try {
            JSONObject addressObjec = new JSONObject(askMe.getAskPosition());
            String province = addressObjec.getString("province");
            String city = addressObjec.getString("city");
            String district = addressObjec.getString("district");
            String township = addressObjec.getString("township");
            String detail = addressObjec.getString("detail");
            String aDetail = "";
            if (addressObjec.has("adetail"))
                aDetail = addressObjec.getString("adetail");
            addressItemViewModel.address.set(province + city + district + township + detail + aDetail);
            getMapViewWrapper().setAddress(askMe.getAskPosition());
            getMapViewWrapper().setDetailAddress(aDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMapViewWrapper().initWithPosition(askMe.getGeoX(), askMe.getGeoY());
    }

    public MapViewWrapper getMapViewWrapper() {
        return wrapper;
    }

    public void setObserblePreview(final PreviewViewModel previewViewModel) {
        tagItemViewModel.contentString.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.title.clear();
                previewViewModel.title.addAll(tagItemViewModel.content);
            }
        });
        shopItemViewModel.content.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.shop.set(shopItemViewModel.content.get());
            }
        });
        addressItemViewModel.address.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.address.set(addressItemViewModel.address.get());
            }
        });
        priceItemViewModel.content.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.price.set(priceItemViewModel.content.get());
            }
        });
        reasonItemViewModel.content.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.desc.set(reasonItemViewModel.content.get());
            }
        });
        detailItemViewModel.content.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                previewViewModel.detial.set(detailItemViewModel.content.get());
            }
        });
        detailItemViewModel.tags.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<DetialTagViewModel>>() {
            @Override
            public void onChanged(ObservableList<DetialTagViewModel> detialTagViewModels) {
                if (detialTagViewModels.size() >= 1) {
                    previewViewModel.tags.clear();
                    previewViewModel.tags.addAll(detialTagViewModels.subList(0, detialTagViewModels.size() - 1));
                }

            }

            @Override
            public void onItemRangeChanged(ObservableList<DetialTagViewModel> detialTagViewModels, int i, int i1) {
                if (detialTagViewModels.size() >= 1) {
                    previewViewModel.tags.clear();
                    previewViewModel.tags.addAll(detialTagViewModels.subList(0, detialTagViewModels.size() - 1));
                }
            }

            @Override
            public void onItemRangeInserted(ObservableList<DetialTagViewModel> detialTagViewModels, int i, int i1) {
                if (detialTagViewModels.size() >= 1) {
                    previewViewModel.tags.clear();
                    previewViewModel.tags.addAll(detialTagViewModels.subList(0, detialTagViewModels.size() - 1));
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList<DetialTagViewModel> detialTagViewModels, int i, int i1, int i2) {
                if (detialTagViewModels.size() >= 1) {
                    previewViewModel.tags.clear();
                    previewViewModel.tags.addAll(detialTagViewModels.subList(0, detialTagViewModels.size() - 1));
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList<DetialTagViewModel> detialTagViewModels, int i, int i1) {
                if (detialTagViewModels.size() >= 1) {
                    previewViewModel.tags.clear();
                    previewViewModel.tags.addAll(detialTagViewModels.subList(0, detialTagViewModels.size() - 1));
                }
            }
        });

    }

    class ViewPagerAdapter extends PagerAdapter {
        View[] views;

        public ViewPagerAdapter(View[] views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);//手动添加
            return views[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
