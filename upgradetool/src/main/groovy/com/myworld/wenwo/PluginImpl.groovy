package com.myworld.wenwo

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.avos.avoscloud.AVOSCloud


class PluginImpl implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("upgrade", UpgradeExtension)
        project.task("upgradeTask", type: UploadTask)
        project.task("upgradeInfoTask", type: UploadInfoTask)
    }
}

class UpgradeExtension {
    def versionCode = 1
    def versionName = "1.0.0"
    def versionDesc = "修复部分bug"
    def apkPath = "/build/outputs/apk/app-release.apk"
}