package com.myworld.wenwo.add.preview;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myworld.wenwo.R;
import com.myworld.wenwo.add.AddActivity;
import com.myworld.wenwo.application.Config;
import com.myworld.wenwo.application.WenWoApplication;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.databinding.FragmentPreviewBinding;
import com.myworld.wenwo.detial.DetialTagViewModel;
import com.myworld.wenwo.utils.ObjectCopyUtil;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.utils.QiniuUitl;
import com.myworld.wenwo.view.widget.ImageTimelineView;
import com.myworld.wenwo.view.widget.MapViewWrapper;
import com.myworld.wenwo.view.widget.TitleBar;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import rx.Subscriber;

/**
 * Created by jianglei on 16/7/21.
 */

public class PreviewFragment extends Fragment implements View.OnClickListener {
    private PreviewViewModel previewViewModel;
    private ImageTimelineView imageTimelineView;
    private boolean uploaded;
    private AlertDialog dialog;

    public static PreviewFragment newInstance() {

        Bundle args = new Bundle();

        PreviewFragment fragment = new PreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentPreviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, null, false);
        previewViewModel = new PreviewViewModel(this);
        binding.setPreviewViewModel(previewViewModel);
        View root = binding.getRoot();
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
                if (checkNotNull()) {
                    upload();
                }
            }
        });
        imageTimelineView = (ImageTimelineView) root.findViewById(R.id.desc_image);
        imageTimelineView.setOnClickListener(this);
        dialog = new AlertDialog.Builder(getActivity()).setView(R.layout.dialog_lodding).create();
        return root;
    }

    UpCompletionHandler handler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            if (info.isOK()) {
                Toast(R.string.upload_ask_complete);
            } else {
                Toast.makeText(WenWoApplication.getInstance(), getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
            }
            if (dialog.isShowing())
                dialog.dismiss();
            if (uploaded) {
                EventBus.getInstance().onEevent(AddActivity.class, "sendComplete");
            }
        }
    };

    private void upload() {

        dialog.show();
        ObservableUtil.runOnUI(new rx.Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(doUpload());
            }
        }, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean complete) {
                if (complete) {
                    uploaded = true;
                    if (!TextUtils.isEmpty(imageKey))
                        uploadImage(imageKey, ((AddActivity) getActivity()).getImageData(), handler);
                    else {
                        Toast(R.string.upload_ask_complete);
                        dialog.dismiss();
                        EventBus.getInstance().onEevent(AddActivity.class, "sendComplete");
                    }
                } else {
                    Toast(R.string.upload_ask_failed);
                }
            }
        });
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public PreviewViewModel getPreviewViewModel() {
        return previewViewModel;
    }

    public void showPage(int index) {
        ((AddActivity) getActivity()).showEditDetail(index);
    }

    @Override
    public void onClick(View v) {
        ((AddActivity) getActivity()).selectPicture();
    }

    public void showImage(String uri) {
        imageTimelineView.setLineUri(uri);
    }

    public void showImage(byte[] data) {
        imageTimelineView.setImage(data);
    }

    public boolean checkNotNull() {
        if (previewViewModel.title.size() <= 0) {
            Toast(R.string.title_is_empty);
            return false;
        }
        if (isEmpty(previewViewModel.desc)) {
            Toast(R.string.reason_is_empty);
            return false;
        }
        if (isEmpty(previewViewModel.detial)) {
            Toast(R.string.detail_is_empty);
            return false;
        }
        if (isEmpty(previewViewModel.shop)) {
            Toast(R.string.shop_is_empty);
            return false;
        }
        if (isEmpty(previewViewModel.address)) {
            Toast(R.string.address_is_empty);
            return false;
        }
        if (isEmpty(previewViewModel.price)) {
            Toast(R.string.price_is_empty);
            return false;
        }
        return true;
    }

    public void Toast(@StringRes int strId) {
        Toast.makeText(getActivity(), getString(strId), Toast.LENGTH_SHORT).show();
    }

    public AskMe getAskMe() {
        AskMe askMe = new AskMe();
        askMe.setCreateByName(Config.USERNAME);
        if (isEdit())
            askMe = ObjectCopyUtil.copyAskMe(((AddActivity) getActivity()).getAskMe());
        if (!isEmpty(previewViewModel.shop))
            askMe.setShopName(previewViewModel.shop.get());
        if (previewViewModel.title.size() > 0)
            askMe.setAskTagStr(getTagStr(previewViewModel.title));
        askMe.setAskReason(previewViewModel.desc.get());
        try {
            askMe.setAskContentShow(getContentStr(previewViewModel.detial.get(), previewViewModel.tags));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MapViewWrapper wrapper = ((AddActivity) getActivity()).getMapViewWrapper();
        if (!TextUtils.isEmpty(wrapper.getAddress())) {
            askMe.setAskPosition(wrapper.getAddress());
            askMe.setGeoX(wrapper.getGeoX());
            askMe.setGeoY(wrapper.getGeoY());
        }

        try {
            askMe.setAskPrice(Float.valueOf(previewViewModel.price.get()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return askMe;
    }

    private String imageKey;

    public boolean doUpload() {
        AskMe askMe = getAskMe();
        final byte[] imageData = ((AddActivity) getActivity()).getImageData();
        Log.d("___________upload ask:", askMe.toString());
        if (imageData != null && imageData.length > 0) {
            String key = "/" + Config.USERNAME + "/" + System.currentTimeMillis() + ".jpg";
            String json = "[{\"image\":\"http://o83np3eq2.bkt.clouddn.com/" + key + "\"}]";
            askMe.setAskImage(json);
            imageKey = key;
        }
        if (isEdit())
            return AskMeRepository.getInstance().editAsk(askMe, Config.USERNAME);
        else
            return AskMeRepository.getInstance().sendAsk(askMe, Config.USERNAME);
    }

    public void uploadImage(String key, final byte[] imageData, final UpCompletionHandler handler) {
        try {
            QiniuUitl.uploadFile(imageData, key, new UpCompletionHandler() {
                int retryTimes = 0;

                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (!info.isOK()) {
                        if (retryTimes > 3) {
                            Toast.makeText(WenWoApplication.getInstance(), getString(R.string.upload_complete), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            QiniuUitl.uploadFile(imageData, key, this);
                            retryTimes++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (handler != null)
                            handler.complete(key, info, response);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEdit() {
        return ((AddActivity) getActivity()).isEdit();
    }

    public boolean edited() {
        return previewViewModel.title.size() > 0 || !isEmpty(previewViewModel.address) || !isEmpty(previewViewModel.price)
                || !isEmpty(previewViewModel.detial) || !isEmpty(previewViewModel.shop);
    }

    public boolean isEmpty(Observable field) {
        if (field instanceof ObservableField)
            return TextUtils.isEmpty(((ObservableField<String>) field).get());
        else if (field instanceof ObservableList) {
            return ((ObservableList) field).size() <= 0;
        }
        return true;
    }

    public String getContentStr(String detail, ObservableList<DetialTagViewModel> tags) throws JSONException {
        JSONArray array = new JSONArray();
        for (DetialTagViewModel detialTagViewModel : tags) {
            String name = detialTagViewModel.tagName.get();
            String val = detialTagViewModel.tagValue.get();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(val))
                continue;
            JSONObject tagObject = new JSONObject();
            tagObject.put("name", name);
            tagObject.put("val", val);
            array.put(tagObject);
        }
        JSONObject object = new JSONObject();

        object.put("detail", detail);
        object.put("detailLi", array);
        return object.toString();
    }

    public String getTagStr(List<String> tags) {
        JSONArray jsonArray = new JSONArray();
        for (String tag : tags) {
            JSONObject objetc = new JSONObject();
            try {
                objetc.put("tag_name", tag);
                jsonArray.put(objetc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }
}
