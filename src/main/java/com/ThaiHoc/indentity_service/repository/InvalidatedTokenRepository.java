package com.ThaiHoc.indentity_service.repository;

import com.ThaiHoc.indentity_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
}
