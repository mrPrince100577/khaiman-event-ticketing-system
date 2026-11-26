package com.stj.khaiman.auth.repository;

import com.stj.khaiman.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    @Query(
            """
           SELECT r
           FROM RefreshToken r
           WHERE r.sid = :sid
"""
    )
    Optional<RefreshToken> findBySessionId(String sid);

    //return number of deleted rows: 0 = token was already used (or never existed)
    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.jti = :jti")
    int deleteByJti(@Param("jti") String jti);
}
