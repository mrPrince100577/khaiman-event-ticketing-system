package com.stj.khaiman.user.config;

import com.stj.khaiman.user.constants.RoleNames;
import com.stj.khaiman.user.entity.PermissionEntity;
import com.stj.khaiman.user.entity.RoleEntity;
import com.stj.khaiman.user.entity.RoleHasPermission;
import com.stj.khaiman.user.repository.PermissionRepository;
import com.stj.khaiman.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Tạo sẵn role và permission theo tài liệu "Ma trận phân quyền RBAC" mỗi khi app khởi động.
 *
 * <p>Chỉ thêm những gì còn thiếu, nên chạy lại nhiều lần vẫn an toàn:
 * <ul>
 *   <li>Permission chưa có (theo {@code code}) thì tạo mới.</li>
 *   <li>Role chưa có (theo {@code roleName}) thì tạo mới kèm các permission của nó.
 *       Role đã tồn tại thì giữ nguyên, vì admin có thể đã chỉnh quyền của role đó.</li>
 * </ul>
 */
@Slf4j
@Component
@Order(1)   // chạy trước AdminAccountInitializer (@Order(2)) vì admin cần role ADMIN
@RequiredArgsConstructor
public class RbacDataInitializer implements CommandLineRunner {

    private record PermissionSeed(String code, String module, String description) {}

    private record RoleSeed(String name, String description, List<String> permissionCodes) {}

    private static final List<PermissionSeed> PERMISSIONS = List.of(
            new PermissionSeed("EVENT_CREATE", "EVENT", "Tạo sự kiện mới"),
            new PermissionSeed("EVENT_UPDATE", "EVENT", "Sửa sự kiện, suất diễn, khu vực; gửi sự kiện đi duyệt"),
            new PermissionSeed("EVENT_CANCEL", "EVENT", "Đóng (huỷ) sự kiện"),
            new PermissionSeed("EVENT_APPROVE", "EVENT", "Duyệt hoặc từ chối sự kiện"),
            new PermissionSeed("TICKET_TIER_MANAGE", "TICKET", "Tạo, sửa hạng vé"),
            new PermissionSeed("BOOKING_CREATE", "BOOKING", "Đặt vé"),
            new PermissionSeed("BOOKING_VIEW_EVENT", "BOOKING", "Xem đơn đặt vé của một sự kiện"),
            new PermissionSeed("USER_MANAGE", "USER", "Khoá, mở khoá tài khoản"),
            new PermissionSeed("ROLE_MANAGE", "USER", "Quản lý role và permission"),
            new PermissionSeed("DASHBOARD_VIEW", "DASHBOARD", "Xem số liệu thống kê"));

    private static final List<RoleSeed> ROLES = List.of(
            new RoleSeed(RoleNames.CUSTOMER, "Khách mua vé", List.of(
                    "BOOKING_CREATE")),
            new RoleSeed(RoleNames.ORGANIZER, "Ban tổ chức sự kiện", List.of(
                    "EVENT_CREATE", "EVENT_UPDATE", "EVENT_CANCEL", "TICKET_TIER_MANAGE",
                    "BOOKING_CREATE", "BOOKING_VIEW_EVENT", "DASHBOARD_VIEW")),
            new RoleSeed(RoleNames.ADMIN, "Quản trị viên hệ thống", List.of(
                    "EVENT_CANCEL", "EVENT_APPROVE", "BOOKING_VIEW_EVENT",
                    "USER_MANAGE", "ROLE_MANAGE", "DASHBOARD_VIEW")));

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, PermissionEntity> permissionsByCode = seedPermissions();
        seedRoles(permissionsByCode);
    }

    private Map<String, PermissionEntity> seedPermissions() {
        Map<String, PermissionEntity> permissionsByCode = new HashMap<>();
        for (PermissionSeed seed : PERMISSIONS) {
            PermissionEntity permission = permissionRepository.findByCode(seed.code())
                    .orElseGet(() -> {
                        log.info("Creating permission {}", seed.code());
                        return permissionRepository.save(PermissionEntity.builder()
                                .code(seed.code())
                                .module(seed.module())
                                .description(seed.description())
                                .build());
                    });
            permissionsByCode.put(seed.code(), permission);
        }
        return permissionsByCode;
    }

    private void seedRoles(Map<String, PermissionEntity> permissionsByCode) {
        for (RoleSeed seed : ROLES) {
            if (roleRepository.findByRoleName(seed.name()).isPresent()) {
                continue;
            }

            RoleEntity role = RoleEntity.builder()
                    .roleName(seed.name())
                    .description(seed.description())
                    .builtIn(true)
                    .roleHasPermissions(new HashSet<>())
                    .build();

            for (String code : seed.permissionCodes()) {
                role.getRoleHasPermissions().add(RoleHasPermission.builder()
                        .role(role)
                        .permission(permissionsByCode.get(code))
                        .build());
            }

            // cascade = ALL trên roleHasPermissions nên lưu role là lưu luôn các dòng role_has_permissions
            roleRepository.save(role);
            log.info("Created role {} with {} permissions", seed.name(), seed.permissionCodes().size());
        }
    }
}
