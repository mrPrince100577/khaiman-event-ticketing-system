package com.stj.khaiman.user.config;

import com.stj.khaiman.user.constants.RoleNames;
import com.stj.khaiman.user.entity.RoleEntity;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.enums.UserStatus;
import com.stj.khaiman.user.repository.RoleRepository;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tạo tài khoản admin đầu tiên khi app khởi động.
 *
 * <p>Thông tin đăng nhập lấy từ cấu hình {@code app.admin.*} (biến môi trường ADMIN_USERNAME,
 * ADMIN_EMAIL, ADMIN_PASSWORD), không viết cứng trong code.
 * <ul>
 *   <li>Chưa đặt ADMIN_PASSWORD thì bỏ qua.</li>
 *   <li>Đã có user trùng username hoặc email thì bỏ qua, kể cả khi mật khẩu trong cấu hình đã đổi.</li>
 * </ul>
 *
 * <p>Chạy sau {@link RbacDataInitializer} ({@code @Order(2)}) vì cần role ADMIN đã được tạo.
 */
@Slf4j
@Component
@Order(2)
public class AdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String email;
    private final String password;

    public AdminAccountInitializer(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder,
                                   @Value("${app.admin.username:}") String username,
                                   @Value("${app.admin.email:}") String email,
                                   @Value("${app.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (password.isBlank()) {
            log.warn("ADMIN_PASSWORD is not set, skipping admin account creation");
            return;
        }
        if (username.isBlank() || email.isBlank()) {
            log.warn("ADMIN_USERNAME or ADMIN_EMAIL is not set, skipping admin account creation");
            return;
        }
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            return;
        }

        RoleEntity adminRole = roleRepository.findByRoleName(RoleNames.ADMIN)
                .orElseThrow(() -> new IllegalStateException("Role ADMIN has not been seeded"));

        UserEntity admin = UserEntity.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .userStatus(UserStatus.ACTIVE)
                .build();
        admin.addRole(adminRole);

        // cascade = ALL trên userHasRoles nên lưu user là lưu luôn dòng user_has_role
        userRepository.save(admin);
        log.info("Created admin account {}", username);   // không bao giờ log mật khẩu
    }
}
