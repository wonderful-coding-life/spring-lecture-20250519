package com.example.demo.repository;

import com.example.demo.model.AccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessKeyRepository extends JpaRepository<AccessKey, String> {
}
