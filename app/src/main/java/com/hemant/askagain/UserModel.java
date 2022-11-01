package com.hemant.askagain;

public class UserModel {
    String name;
    String  profilePic;
    String email;
    String contactNumber;
    String profession;
    String gender;

    public UserModel() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getGender() {
         return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserModel(String name, String profilePic, String email) {
        this.name = name;
        this.profilePic = profilePic;
        this.email = email;
    }



}
