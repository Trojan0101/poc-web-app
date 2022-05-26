package com.assesment.poc.repository;

import com.assesment.poc.model.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDetailsRepository extends JpaRepository<LoginDetails, String> {

}
