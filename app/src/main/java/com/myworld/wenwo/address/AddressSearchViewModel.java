package com.myworld.wenwo.address;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.myworld.wenwo.BR;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.*;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.found.FoundFragment;
import com.myworld.wenwo.splash.SplashActivity;
import com.myworld.wenwo.utils.ObservableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by jianglei on 16/9/1.
 */

public class AddressSearchViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> cityText = new ObservableField<>();
    public ObservableField<String> addressText = new ObservableField<>("正在定位...");
//    public ObservableDouble geoX=new ObservableDouble();
//    public ObservableDouble geoY=new ObservableDouble();
//    public ObservableField<String> street=new ObservableField<>();
    private BDLocation bdLocation;
    public ObservableList<AddressItemViewModel> items = new ObservableArrayList<>();
    public ItemViewSelector<AddressItemViewModel> itemView = new ItemViewSelector<AddressItemViewModel>() {
        @Override
        public void select(ItemView itemView, int position, AddressItemViewModel item) {
            if (position == 0)
                itemView.set(BR.itemViewModel, R.layout.search_address_item_first);
            else
                itemView.set(BR.itemViewModel, R.layout.search_address_item);
        }

        @Override
        public int viewTypeCount() {
            return 2;
        }
    };

    public ReplyCommand myLocationClick=new ReplyCommand(new Action0() {
        @Override
        public void call() {
            Config.geo_latitude = bdLocation.getLatitude();
            Config.geo_longtitude = bdLocation.getLongitude();
            Config.geo_address.set(bdLocation.getStreet()+bdLocation.getStreetNumber());
            EventBus.getInstance().onEevent(FoundFragment.class,"reload");
            ((AddressSearchActivity)context).finish();
        }
    });
    public AddressSearchViewModel(Context context) {
        this.context = context;
        findHost();
        getLocation();
    }
    private void getLocation(){
        final LocationClient client = new LocationClient(context);
        SplashActivity.initLocation(client);
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("position", "lat:" + bdLocation.getLatitude() + "______longt" + bdLocation.getLongitude());
                AddressSearchViewModel.this.bdLocation=bdLocation;
                cityText.set(bdLocation.getCity());
                addressText.set(bdLocation.getDistrict()+bdLocation.getStreet()+bdLocation.getStreetNumber());
                client.stop();
            }
        });
        client.start();
    }

    private void findHost() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<List<AddressItemViewModel>>() {
            @Override
            public void call(Subscriber<? super List<AddressItemViewModel>> subscriber) {
                String result = AskMeRepository.getInstance().getHostAddress();
                List<AddressItemViewModel> itemViewModels = new ArrayList<AddressItemViewModel>();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") != 200)
                        return;
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        AddressItemViewModel itemViewModel = new AddressItemViewModel(context);
                        itemViewModel.addressText.set(item.getString("address"));
                        itemViewModel.geoX.set(item.getDouble("GeoX"));
                        itemViewModel.geoY.set(item.getDouble("GeoY"));
                        itemViewModel.hot.set(item.getInt("hot"));
                        itemViewModels.add(itemViewModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(itemViewModels);
            }
        }, new Subscriber<List<AddressItemViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<AddressItemViewModel> addressItemViewModels) {
                items.clear();
                items.addAll(addressItemViewModels);
            }
        });
    }
}
