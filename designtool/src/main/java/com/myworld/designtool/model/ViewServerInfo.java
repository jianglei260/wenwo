package com.myworld.designtool.model;

/**
 * Created by jianglei on 16/9/11.
 */
public class ViewServerInfo {
    public final int protocolVersion;
    public final int serverVersion;

    public ViewServerInfo(int serverVersion, int protocolVersion) {
        this.protocolVersion = protocolVersion;
        this.serverVersion = serverVersion;
    }
}
