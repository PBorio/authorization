package com.borio.authorization.repositories;

import com.borio.authorization.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("select v from VerificationToken v where v.token = :token and v.expireDate >= :expireDate")
    List<VerificationToken> findByToken(String token, Date expireDate);

}
