package com.qad.posbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qad.posbe.domain.Role;
import com.qad.posbe.domain.User;
import com.qad.posbe.domain.request.CreateUserDTO;
import com.qad.posbe.domain.request.UpdateUserDTO;
import com.qad.posbe.domain.request.ChangePasswordDTO;
import com.qad.posbe.domain.response.ResCreateUserDTO;
import com.qad.posbe.domain.response.ResUpdateUserDTO;
import com.qad.posbe.domain.response.ResUserDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.repository.UserRepository;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.error.InvalidPasswordException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;


    public User handleCreateUser(User user, MultipartFile avatarFile) {
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = this.cloudinaryService.uploadImage(avatarFile);
            user.setAvatar(avatarUrl);
        }


        if(user.getRole() != null){
            Role r = this.roleService.fetchRoleById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        }

        return this.userRepository.save(user);
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO handleGetUser(Specification<User> userSpec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(userSpec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        rs.setMeta(meta);

        List<ResUserDTO> listUser  = pageUser.getContent()
                        .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public User handleUpdateUser(Long userId, UpdateUserDTO reqUser, MultipartFile avatarFile) {
        User currentUser = this.fetchUserById(userId);

        if (currentUser!=null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setName(reqUser.getName());
            currentUser.setPhoneNumber(reqUser.getPhoneNumber());
            currentUser.setEmail(reqUser.getEmail());

            if(avatarFile != null && !avatarFile.isEmpty()){
                String avatarUrl = this.cloudinaryService.uploadImage(avatarFile);
                currentUser.setAvatar(avatarUrl);
            }

            if(reqUser.getRoleId() != null){
                Role r = this.roleService.fetchRoleById(reqUser.getRoleId());
                currentUser.setRole(r != null ? r : null);
            }
        }
        currentUser = this.userRepository.save(currentUser);
        return currentUser;
    }

    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public User getCurrentUser() {
        String username = SecurityUtil.getCurrentUserLogin()
            .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thông tin người dùng hiện tại"));
        User user = handleGetUserByUserName(username);
        if (user == null) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với username: " + username);
        }
        return user;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();

        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setUsername(user.getUsername());
        resCreateUserDTO.setName(user.getName());
        resCreateUserDTO.setPhoneNumber(user.getPhoneNumber());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setCreatedAt(user.getCreatedAt());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setAvatar(user.getAvatar());
        resCreateUserDTO.setRoleId(user.getRole().getId());

        return resCreateUserDTO;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();

        resUpdateUserDTO.setId(user.getId());
        resUpdateUserDTO.setName(user.getName());
        resUpdateUserDTO.setPhoneNumber(user.getPhoneNumber());
        resUpdateUserDTO.setEmail(user.getEmail());
        resUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        resUpdateUserDTO.setGender(user.getGender());
        resUpdateUserDTO.setAddress(user.getAddress());

        return resUpdateUserDTO;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();

        resUserDTO.setId(user.getId());
        resUserDTO.setUsername(user.getUsername());
        resUserDTO.setName(user.getName());
        resUserDTO.setPhoneNumber(user.getPhoneNumber());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setAddress(user.getAddress());

        if(user.getRole() != null){
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            resUserDTO.setRole(roleUser);
        }

        resUserDTO.setAvatar(user.getAvatar());


        return resUserDTO;
    }


    public User toEntity(CreateUserDTO formRequest) {
        return User.builder()
                .name(formRequest.getName())
                .username(formRequest.getUsername())
                .password(formRequest.getPassword())
                .phoneNumber(formRequest.getPhoneNumber())
                .email(formRequest.getEmail())
                .gender(formRequest.getGender())
                .address(formRequest.getAddress())
                .role(formRequest.getRoleId() != null ? Role.builder().id(formRequest.getRoleId()).build() : null)
                .build();
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUserName(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String username){
        return this.userRepository.findByRefreshTokenAndUsername(token, username);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordDTO changePasswordDTO) {
        // Kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp nhau
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new InvalidPasswordException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // Lấy thông tin user
        User user = this.userRepository.findByUsername(username);
        if(user == null){
            throw new EntityNotFoundException("Không tìm thấy người dùng");
        }

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Mật khẩu cũ không đúng");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }
}
