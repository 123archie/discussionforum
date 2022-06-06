package com.hemant.askagain;

import android.net.Uri;

public class User {
    String name;
    Uri profilePic;

    public User() {
    }

    public User(String name, Uri profilePic) {
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }
}
