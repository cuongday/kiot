package com.qad.posbe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.qad.posbe.domain.User;
import com.qad.posbe.domain.request.ReqLoginDTO;
import com.qad.posbe.domain.response.ResCreateUserDTO;
import com.qad.posbe.domain.response.ResLoginDTO;
import com.qad.posbe.service.UserService;
import com.qad.posbe.util.SecurityUtil;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.error.IdInvalidException;
import org.springframework.security.access.prepost.PreAuthorize;
import com.qad.posbe.domain.request.ChangePasswordDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${qad.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
        try {
            //Nạp input gồm username/password vào Security
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword());
            
            //xác thực người dùng
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            
            // Kiểm tra user có tồn tại trong DB
            User currentUserDB = this.userService.handleGetUserByUserName(loginDTO.getUsername());
            if(currentUserDB == null) {
                throw new UsernameNotFoundException("Tài khoản không tồn tại trong hệ thống");
            }

            // create token và set authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ResLoginDTO res = new ResLoginDTO();

            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getUsername(),
                    currentUserDB.getName(),
                    currentUserDB.getPhoneNumber(),
                    currentUserDB.getEmail(),
                    currentUserDB.getAvatar(),
                    currentUserDB.getRole()
            );
            res.setUser(userLogin);

            String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
            res.setAccessToken(access_token);

            // create a refresh token
            String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);
            //update user
            this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

            //set cookies
            ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
                    .httpOnly(true)
                    .maxAge(refreshTokenExpiration)
                    .path("/")
                    .build();

            res.setRefreshToken(refresh_token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                    .body(res);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác");
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra trong quá trình đăng nhập: " + e.getMessage());
        }
    }


    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUserDB = this.userService.handleGetUserByUserName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount(userLogin);
        if(currentUserDB != null){
            userLogin.setId(currentUserDB.getId());
            userLogin.setUsername(currentUserDB.getUsername());
            userLogin.setName(currentUserDB.getName());
            userLogin.setAvatar(currentUserDB.getAvatar());
            userLogin.setPhoneNumber(currentUserDB.getPhoneNumber());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setRole(currentUserDB.getRole());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token", defaultValue = "default") String refreshToken) throws IdInvalidException {
        if(refreshToken.equals("default")){
            throw new IdInvalidException("Bạn không có Refresh token Cookie");
        }

        // check valid
        Jwt decodedToken =  this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // check user by token and email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if(currentUser == null){
            throw new IdInvalidException("Refresh token không hợp lệ");
        }



        //issue new token/set refresh token as cookies
        ResLoginDTO res = new ResLoginDTO();

        User currentUserDB = this.userService.handleGetUserByUserName(email);
        if(currentUserDB != null){
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getUsername(),
                    currentUserDB.getPhoneNumber(),
                    currentUserDB.getEmail(),
                    currentUserDB.getName(),
                    currentUserDB.getAvatar(),
                    currentUserDB.getRole()
            );
            res.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(email, res);


        res.setAccessToken(access_token);

        // create a refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, res);
        //update user
        this.userService.updateUserToken(new_refresh_token, email);

        //set cookies
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", new_refresh_token)
                .httpOnly(true)
                .maxAge(refreshTokenExpiration)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);

    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        if(email.equals("")){
            throw new IdInvalidException("Access token không hợp lệ");
        }

        //update refresh token = null
        this.userService.updateUserToken(null, email);

        //remove refresh cookies
        ResponseCookie deleteSpringCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .build();
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User postmanUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.existsByUsername(postmanUser.getUsername());
        if(isEmailExist){
            throw new IdInvalidException("Username " + postmanUser.getUsername() +" đã tồn tại, vui lòng chọn username khác");
        }

        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(postmanUser, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    @ApiMessage("Đổi mật khẩu thành công")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        String username = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng hiện tại"));
        
        userService.changePassword(username, changePasswordDTO);
        return ResponseEntity.ok().build();
    }
}
