package com.assesment.poc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}
