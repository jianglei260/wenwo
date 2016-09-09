package com.myworld.wenwo.data.source.local;

import android.content.Context;

import com.google.gson.Gson;
import com.myworld.wenwo.data.entity.AuthInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import retrofit2.http.Field;

/**
 * Created by jianglei on 16/7/29.
 */

public class AuthLocalDataSource {
    public static AuthInfo getAuthInfo(Context context) {
        Gson gson = new Gson();
        String authDataPath = context.getFilesDir().getAbsolutePath() + "/auth.json";
        File authData = new File(authDataPath);
        if (!authData.exists())
            return null;
        try {
            Reader reader = new FileReader(authData);
            return gson.fromJson(reader, AuthInfo.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(Context context) {
        String authDataPath = context.getFilesDir().getAbsolutePath() + "/auth.json";
        File authData = new File(authDataPath);
        if (authData.exists())
            authData.delete();
    }

    public static void saveAuthInfo(Context context, String authInfo) {
        String authDataPath = context.getFilesDir().getAbsolutePath() + "/auth.json";
        File authData = new File(authDataPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(authData, false);
            byte[] data = authInfo.getBytes();
            fos.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
