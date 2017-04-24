package com.boost.transitionanimation;


import android.os.Parcel;
import android.os.Parcelable;
import android.transition.Slide;

class Profile implements Parcelable{

    private String fullName;
    private String coverUrl;
    private String avatarUrl;
    private String bio;
    private String transitionName;

    public Profile() {
    }

    protected Profile(Parcel in) {
        fullName = in.readString();
        coverUrl = in.readString();
        avatarUrl = in.readString();
        bio = in.readString();
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "fullName='" + fullName + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(coverUrl);
        dest.writeString(avatarUrl);
        dest.writeString(bio);
    }

    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
    }
}
