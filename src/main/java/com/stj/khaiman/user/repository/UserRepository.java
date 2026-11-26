package com.stj.khaiman.user.repository;

import com.stj.khaiman.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            """
        SELECT COUNT(u) > 0
        FROM UserEntity u
        WHERE u.username = :username
        OR u.email = :email
    """
    )
    boolean existsByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @EntityGraph(attributePaths = "userHasRoles.role.roleHasPermissions.permission")
    Optional<UserEntity> findByUsername(String username);

    @EntityGraph(attributePaths = "userHasRoles.role.roleHasPermissions.permission")
    Optional<UserEntity> findWithRolesById(Long id);

    @Query("""
        SELECT u
        FROM UserEntity u
        LEFT JOIN UserProfile p ON p.user = u
        WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<UserEntity> search(@Param("keyword") String keyword, Pageable pageable);
}
