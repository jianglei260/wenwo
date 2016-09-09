package com.myworld.wenwo.add;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.myworld.wenwo.R;
import com.myworld.wenwo.add.edit.EditFragment;
import com.myworld.wenwo.add.preview.PreviewFragment;
import com.myworld.wenwo.add.preview.PreviewViewModel;
import com.myworld.wenwo.bus.EventBus;
import com.myworld.wenwo.bus.OnEvent;
import com.myworld.wenwo.data.entity.AskMe;
import com.myworld.wenwo.data.repository.AskMeRepository;
import com.myworld.wenwo.detial.DetialActivity;
import com.myworld.wenwo.utils.BitmapUtil;
import com.myworld.wenwo.utils.ObservableUtil;
import com.myworld.wenwo.utils.ViewServer;
import com.myworld.wenwo.view.widget.MapViewWrapper;
import com.myworld.wenwo.view.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;

public class AddActivity extends AppCompatActivity {
    private PreviewFragment previewFragment;
    private EditFragment editFragment;
    private TitleBar titleBar;
    private boolean first = true;
    private byte[] imageData;
    private boolean edit;
    private AskMe askMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("addAcitivty", "oncreate");
        super.onCreate(savedInstanceState);
        ViewServer.get(this).addWindow(this);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_add);

        previewFragment = PreviewFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container, previewFragment, "preview").commit();
        editFragment = EditFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.container, editFragment, "edit").commit();
        getFragmentManager().beginTransaction().hide(editFragment).commit();
        edit = getIntent().getBooleanExtra("edit", false);
        if (edit) {
            askMe = AskMeRepository.getInstance().getAsk(getIntent().getStringExtra("objectId"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initWithData(askMe);
                }
            }, 100);
        } else {
            readNotSend();
        }
        EventBus.getInstance().regist(this);
    }

    public AskMe getAskMe() {
        return askMe;
    }

    public static void startWithEdit(Context context, AskMe askMe) {
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra("edit", true);
        intent.putExtra("objectId", askMe.getObjectId());
        context.startActivity(intent);
    }

    @OnEvent("sendComplete")
    public void sendComplete() {
        if (isEdit()) {
            EventBus.getInstance().onEevent(DetialActivity.class, "sendComplete");
        }
        finish();
    }

    public boolean isEdit() {
        return edit;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public PreviewViewModel getPreviewViewModel() {
        return previewFragment.getPreviewViewModel();
    }

    public void showEditDetail(int index) {
        getFragmentManager().beginTransaction().hide(previewFragment).show(editFragment).commit();
        editFragment.showPage(index);
    }

    public MapViewWrapper getMapViewWrapper() {
        return editFragment.getMapViewWrapper();
    }

    private boolean confirm = false;

    @Override
    public void finish() {
        if (!confirm && previewFragment.edited() && !previewFragment.isUploaded()) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.confirm_back)).setMessage(getString(R.string.confirm_back_detail))
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AskMeRepository.saveAskData(AddActivity.this, previewFragment.getAskMe());
                            dialog.dismiss();
                            confirm = true;
                            finish();
                        }
                    }).setNegativeButton(R.string.give_up, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AskMeRepository.removeData(AddActivity.this);
                            confirm = true;
                            dialog.dismiss();
                            finish();
                        }
                    }).create();
            dialog.show();
            return;
        }
        super.finish();
        overridePendingTransition(R.anim.alpha_in_anim, R.anim.add_close_anim);
    }

    @Override
    public void onBackPressed() {
        if (editFragment != null && !editFragment.isHidden()) {
            getFragmentManager().beginTransaction().hide(editFragment).show(previewFragment).commit();
            return;
        }
        super.onBackPressed();
    }

    public void selectPicture() {
        String[] items = getResources().getStringArray(R.array.pic_items);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.select_pic))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectFrom(which);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public void readNotSend() {
        ObservableUtil.runOnUI(new Observable.OnSubscribe<AskMe>() {
            @Override
            public void call(Subscriber<? super AskMe> subscriber) {
                subscriber.onNext(AskMeRepository.readAsk(AddActivity.this));
            }
        }, new Subscriber<AskMe>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(AskMe askMe) {
                if (askMe != null) {
                    notSendNotify(askMe);
                }
            }
        });
    }

    @OnEvent("initWithData")
    public void initWithData(AskMe askMe) {
        Log.d("addAcitivty", "initWithData");
        if (editFragment != null) {
            editFragment.initViewModel(askMe);
            String image = getImage(askMe.getAskImage());
            if (!TextUtils.isEmpty(image)) ;
            previewFragment.showImage(image);
        }

    }

    private String getImage(String images) {
        try {
            JSONArray jsonArray = new JSONArray(images);
            return jsonArray.getJSONObject(0).getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void notSendNotify(final AskMe askMe) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.not_send).setMessage(R.string.not_send_detail).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editFragment != null) {
                    editFragment.initViewModel(askMe);
                    String image = getImage(askMe.getAskImage());
                    if (!TextUtils.isEmpty(image)) ;
                    previewFragment.showImage(image);
                }

                dialog.dismiss();
            }
        }).create();
        dialog.show();
    }

    private File tempFile;
    public static final int REQUEST_GALLERY = 0;
    public static final int REQUEST_CAMMERA = 1;
    public static final int REQUEST_CUT = 2;

    public void selectFrom(int which) {
        if (which == 0) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, REQUEST_GALLERY);
        } else if (which == 1) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            String fileName = "/wenwo_pic_" + System.currentTimeMillis() + ".jpg";
            tempFile = new File(Environment.getExternalStoragePublicDirectory("/wenwo/image"),
                    fileName);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAMMERA);
        }
    }

    private static int MAX_LENGTH = 720;
    Uri notCorpUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                notCorpUri = data.getData();
                crop(notCorpUri);
            }
        } else if (requestCode == REQUEST_CAMMERA) {
            // 从相机返回的数据
            notCorpUri = Uri.fromFile(tempFile);
            crop(notCorpUri);
        } else if (requestCode == REQUEST_CUT) {
            Uri uri;
            if (data != null) {
                try {
                    if (data.getData() == null)
                        uri = notCorpUri;
                    else
                        uri = data.getData();
                    imageData = BitmapUtil.getBitmapFormUri(this, uri);
                    previewFragment.showImage(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (tempFile != null)
                    // 将临时文件删除
                    tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    protected void onDestroy() {
//        if (previewFragment.edited())
//            AskMeRepository.saveAskData(this, previewFragment.getAskMe());
        super.onDestroy();
        if (getMapViewWrapper()!=null)
            getMapViewWrapper().onActivityDestory(this);
        ViewServer.get(this).removeWindow(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    /*
             * 剪切图片
             */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 720);
//        intent.putExtra("outputY", 720);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, REQUEST_CUT);
    }
}
