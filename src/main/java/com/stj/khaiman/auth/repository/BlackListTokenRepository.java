package com.stj.khaiman.auth.repository;

import com.stj.khaiman.auth.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, String> {

}
