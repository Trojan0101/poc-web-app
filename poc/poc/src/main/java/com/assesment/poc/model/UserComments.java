package com.assesment.poc.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_comments")
public class UserComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long commentId;
    @NotBlank
    private String email;
    @NotBlank
    @Column(name = "first_name")
    private String firstName;
    @NotBlank
    private String latitude;
    @NotBlank
    private String longitude;
    @Size(max = 2147483640)
    private String comment;
    @Column(name = "picture_name")
    private String picturename;

    public UserComments() {
    }

    public UserComments(String email, String firstName, String latitude, String longitude, String comment, String picturename) {
        this.email = email;
        this.firstName = firstName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.comment = comment;
        this.picturename = picturename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPicturename() {
        return picturename;
    }

    public void setPicturename(String picturename) {
        this.picturename = picturename;
    }
}
