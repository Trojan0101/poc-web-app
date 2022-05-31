package com.assesment.poc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "user_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    @NotBlank
    private String email;
    @NotBlank
    private String latitude;
    @NotBlank
    private String longitude;
    private String comment;
    @Column(name = "picture_name")
    private String picturename;

}
