package com.stj.khaiman.user.repository;

import com.stj.khaiman.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query(
            """
        SELECT p
        FROM UserProfile p
        WHERE p.user.id IN :userIds
"""
    )
    List<UserProfile> findByUserIds(@Param("userIds")Collection<Long> userIds);
}
