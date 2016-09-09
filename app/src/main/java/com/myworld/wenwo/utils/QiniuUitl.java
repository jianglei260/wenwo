package com.myworld.wenwo.utils;

import com.myworld.wenwo.application.Config;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by jianglei on 16/8/9.
 */

public class QiniuUitl {
    private static final String SECRET_KEY = "wvt3V7LrhJi6sPefoL1mRB3PsRV_KUUucd3QNmAz";
    private static final String ACCESS_KEY = "DU0ZyfvC06whs4kFM65I4uGlnDkCeyLMV6ct4NPF";

    public static void uploadFile(byte[] data, String key, UpCompletionHandler handler) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("scope", "wenwo:" + key);
        map.put("deadline", (System.currentTimeMillis() / 1000) + 60 * 60 * 24);
        JSONObject json = new JSONObject(map);
        String ENCODE_JSON = UrlSafeBase64.encodeToString(json.toString());
        String encodeSigned = UrlSafeBase64.encodeToString(HMACSHA1.HmacSHA1Encrypt(ENCODE_JSON, SECRET_KEY));
        String token = ACCESS_KEY + ":" + encodeSigned + ":" + ENCODE_JSON;
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(data, key, token, handler, null);
    }
}
