package com.myworld.wenwo.data.entity;

/**
 * Created by jianglei on 16/7/29.
 */

public class AuthInfo {

    /**
     * unionid : oAiCJwBN6ui5nweRctyW3-hho-OI
     * userID : ozSPpwIh5QOp1mzN9utACXmp5mfA
     * icon : http://wx.qlogo.cn/mmopen/gSnd7CmCaM1kNhlkT4qcmff9ybDCuKVfXwiaPrGH4m760uNxxuR0mqbuOeibhOyKcjia7vY93jeMicxZcfqz6R3nx919EpalYX4a/0
     * expiresTime : 1469759827443
     * token : C8obFa6Uujb7Kp8sJU1rq7V1aBCiNIVSawCT3n2TmZf7WPTVksm-cZ8l77R4aIBA6xQIt0Z_OmyxAkYgLLYxyHl2FJ7RsrSbe81-nqn9frg
     * nickname : 江磊
     * city : Nanchong
     * gender : 0
     * province : Sichuan
     * openid : ozSPpwIh5QOp1mzN9utACXmp5mfA
     * refresh_token : F5GdfpDV-eup-f_EEE9RpK5HCv0_bUAw1gA1AdH1H1UamzSAHjFYZBxWVRfQZEDUJ_Jajfs4QRhyD6h9MpA-3N5aW2kiTxftipPWWjOe3LI
     * country : CN
     * expiresIn : 7200
     */

    private String unionid;
    private String userID;
    private String icon;
    private long expiresTime;
    private String token;
    private String nickname;
    private String city;
    private String gender;
    private String province;
    private String openid;
    private String refresh_token;
    private String country;
    private int expiresIn;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "unionid='" + unionid + '\'' +
                ", userID='" + userID + '\'' +
                ", icon='" + icon + '\'' +
                ", expiresTime=" + expiresTime +
                ", token='" + token + '\'' +
                ", nickname='" + nickname + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", openid='" + openid + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", country='" + country + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
