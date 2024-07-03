package com.example.mobiltriyaj.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Option implements Parcelable {
    private String textTr;
    private String textEn;
    private int nextQuestionId;

    public Option(String textTr, String textEn, int nextQuestionId) {
        this.textTr = textTr;
        this.textEn = textEn;
        this.nextQuestionId = nextQuestionId;
    }

    public String getTextTr() {
        return textTr;
    }

    public String getTextEn() {
        return textEn;
    }

    public int getNextQuestionId() {
        return nextQuestionId;
    }

    protected Option(Parcel in) {
        textTr = in.readString();
        textEn = in.readString();
        nextQuestionId = in.readInt();
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textTr);
        dest.writeString(textEn);
        dest.writeInt(nextQuestionId);
    }
}