package com.example.pasta.Model;

public class Kullanici {
    private String resimurl;
    private String kullaniciadi;
    private String bio;
    private String ad;
    private String id;

    public Kullanici() {
    }

    public Kullanici(String resimurl, String kullaniciadi, String bio, String ad, String id) {
        this.resimurl = resimurl;
        this.kullaniciadi = kullaniciadi;
        this.bio = bio;
        this.ad = ad;
        this.id = id;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
