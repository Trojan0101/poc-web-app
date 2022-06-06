package com.assesment.poc.repository;

import com.assesment.poc.model.UserComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentsRepository extends JpaRepository<UserComments, Long> {

    UserComments findByComment(String comment);

}
