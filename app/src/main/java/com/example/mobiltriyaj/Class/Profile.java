package com.example.mobiltriyaj.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String kullanıcıAdi;
    private String adı;
    private String soyadı;
    private String boyu;
    private String kilosu;
    private String dogumTarihi;
    private String cinsiyet;
    private String kanGrubu;

    // Parametreli yapıcı metod
    public Profile(String kullanıcıAdi, String adı, String soyadı, String boyu, String kilosu, String dogumTarihi, String cinsiyet, String kanGrubu) {
        this.kullanıcıAdi = kullanıcıAdi;
        this.adı = adı;
        this.soyadı = soyadı;
        this.boyu = boyu;
        this.kilosu = kilosu;
        this.dogumTarihi = dogumTarihi;
        this.cinsiyet = cinsiyet;
        this.kanGrubu = kanGrubu;
    }

    // Parametresiz yapıcı metod
    public Profile() {}

    // Getter ve setter metodları
    public String getKullanıcıAdi() {
        return kullanıcıAdi;
    }

    public void setKullanıcıAdi(String kullanıcıAdi) {
        this.kullanıcıAdi = kullanıcıAdi;
    }

    public String getAdı() {
        return adı;
    }

    public void setAdı(String adı) {
        this.adı = adı;
    }

    public String getSoyadı() {
        return soyadı;
    }

    public void setSoyadı(String soyadı) {
        this.soyadı = soyadı;
    }

    public String getBoyu() {
        return boyu;
    }

    public void setBoyu(String boyu) {
        this.boyu = boyu;
    }

    public String getKilosu() {
        return kilosu;
    }

    public void setKilosu(String kilosu) {
        this.kilosu = kilosu;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getKanGrubu() {
        return kanGrubu;
    }

    public void setKanGrubu(String kanGrubu) {
        this.kanGrubu = kanGrubu;
    }

    // Parcelable arayüzünün metodları
    protected Profile(Parcel in) {
        kullanıcıAdi = in.readString();
        adı = in.readString();
        soyadı = in.readString();
        boyu = in.readString();
        kilosu = in.readString();
        dogumTarihi = in.readString();
        cinsiyet = in.readString();
        kanGrubu = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kullanıcıAdi);
        dest.writeString(adı);
        dest.writeString(soyadı);
        dest.writeString(boyu);
        dest.writeString(kilosu);
        dest.writeString(dogumTarihi);
        dest.writeString(cinsiyet);
        dest.writeString(kanGrubu);
    }
}
