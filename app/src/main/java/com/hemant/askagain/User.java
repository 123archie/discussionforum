package com.hemant.askagain;

public class User {
    String name;
    String  profilePic;
    String email;
    String contactNumber;
    String profession;
    String gender;

    public User() {
    }

    public User(String name, String profilePic, String email, String contactNumber, String profession, String gender) {
        this.name = name;
        this.profilePic = profilePic;
        this.email = email;
        this.contactNumber = contactNumber;
        this.profession = profession;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
