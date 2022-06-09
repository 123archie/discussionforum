package com.hemant.askagain;

import android.net.Uri;

public class User {
    String name;
    String  profilePic;

    public User() {
    }

    public User(String name, String profilePic) {
        this.name = name;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
