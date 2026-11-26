package com.stj.khaiman.user.service;

import com.stj.khaiman.common.dto.response.PageResponse;
import com.stj.khaiman.user.dto.response.UserSummaryResponse;
import com.stj.khaiman.user.entity.UserEntity;
import com.stj.khaiman.user.entity.UserProfile;
import com.stj.khaiman.user.repository.UserProfileRepository;
import com.stj.khaiman.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "email");

    private static final int MAX_SIZE = 100;

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getAllUsers(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    ){
        String sortField = SORT_FIELDS.contains(sortBy) ? sortBy : "createdAt";

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        int pageNumber = Math.max(page - 1, 0);

        int pageSize = Math.clamp(size, 1, MAX_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortField));

        String searchKeyword = keyword == null ? "" : keyword.trim();

        Page<UserEntity> userEntityPage = userRepository.search(searchKeyword, pageable);

        List<Long> userIds = userEntityPage.getContent().stream()
                .map(UserEntity::getId)
                .toList();

        Map<Long, UserProfile> profileByUserId = userIds.isEmpty()
                ? Map.of()
                : userProfileRepository.findByUserIds(userIds).stream()
                .collect(Collectors.toMap(p -> p.getUser().getId(), Function.identity()));

        Page<UserSummaryResponse> result = userEntityPage.map(user -> new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                fullName(profileByUserId.get(user.getId())),
                user.getUserStatus(),
                user.getRoleNames()
        ));

        return PageResponse.from(result);
    }

    private String fullName(UserProfile userProfile){
        if(userProfile == null){
            return null;
        }

        String fullName = String.join(" ", userProfile.getFirstName().trim(), userProfile.getLastName().trim());

        return fullName.isBlank() ? null : fullName;
    }
}

