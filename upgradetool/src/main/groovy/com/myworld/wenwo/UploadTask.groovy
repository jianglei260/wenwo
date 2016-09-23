package com.myworld.wenwo

import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback
import com.qiniu.http.Response
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UploadTask extends DefaultTask {
    @TaskAction
    public void uploadApk() {
        def versionCode = project.upgrade.versionCode
        def versionName = project.upgrade.versionName
        def versionDesc = project.upgrade.versionDesc
        def apkPath = project.upgrade.apkPath
        AVOSCloud.initialize("m5GMliz1crSh8mcvEzGycIvt-gzGzoHsz", "FOW9iWV1lqhSy2jtbW1zWEsE", "YKaRGPwYCidp6B7v8Rp8Xr7U");
        //密钥配置
        Auth auth = Auth.create("DU0ZyfvC06whs4kFM65I4uGlnDkCeyLMV6ct4NPF", "wvt3V7LrhJi6sPefoL1mRB3PsRV_KUUucd3QNmAz");
        //创建上传对象
        UploadManager uploadManager = new UploadManager();
        String key = versionName + ".apk";
        String url = "http://o83np3eq2.bkt.clouddn.com/" + key;
        try {
            Response response = uploadManager.put(project.rootDir.getAbsolutePath() + apkPath, key, auth.uploadToken("wenwo"));
            if (response.OK) {
                println("upload apk success!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath} apkurl=${url}")
                AVObject object = new AVObject("ApkVersion")
                object.put("versionCode", versionCode)
                object.put("versionName", versionName)
                object.put("feature", versionDesc)
                object.put("url", url)
                try {
                    object.save()
                    println("upload upgrade info success!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}")
                }catch (Exception e){
                    try {
                        object.save()
                        println("upload upgrade info success!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}")
                    }catch (Exception ex){
                        object.save()
                        println("upload upgrade info faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}" + ex.getMessage())
                    }
                    println("upload upgrade info faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}" + e.getMessage())
                }
            } else {
                println("upload apk faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}" + response.error)
            }
        } catch (Exception e) {
            e.printStackTrace();
            println("upload apk faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}")
        };

    }
}
class UploadInfoTask extends DefaultTask{
    @TaskAction
    public void uploadInfo(){
        def versionCode = project.upgrade.versionCode
        def versionName = project.upgrade.versionName
        def versionDesc = project.upgrade.versionDesc
        def apkPath = project.upgrade.apkPath
        AVOSCloud.initialize("m5GMliz1crSh8mcvEzGycIvt-gzGzoHsz", "FOW9iWV1lqhSy2jtbW1zWEsE", "YKaRGPwYCidp6B7v8Rp8Xr7U");
        String key = versionName + ".apk";
        String url = "http://o83np3eq2.bkt.clouddn.com/" + key;
        AVObject object = new AVObject("ApkVersion")
        object.put("versionCode", versionCode)
        object.put("versionName", versionName)
        object.put("feature", versionDesc)
        object.put("url", url)
        try {
            object.save()
            println("upload upgrade info success!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}")
        }catch (Exception e){
            try {
                object.save()
                println("upload upgrade info success!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}")
            }catch (Exception ex){
                object.save()
                println("upload upgrade info faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}" + ex.getMessage())
            }
            println("upload upgrade info faild!!! apkpath=${project.rootDir.getAbsolutePath() + apkPath}" + e.getMessage())
        }
    }
}