package com.SCM.IOMS.service.impl;

import com.SCM.IOMS.dto.request.LoginRequest;
import com.SCM.IOMS.dto.response.AuthResponse;
import com.SCM.IOMS.entity.Role;
import com.SCM.IOMS.entity.User;
import com.SCM.IOMS.repository.UserRepository;
import com.SCM.IOMS.security.JwtTokenProvider;
import com.SCM.IOMS.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new BadCredentialsException("Tên đăng nhập và mật khẩu không được để trống");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Tài khoản không tồn tại"));

        List<String> roles = user.getRoles() == null ? List.of() : user.getRoles().stream()
                .map(role -> role.getRoleCode() != null && !role.getRoleCode().isBlank()
                        ? role.getRoleCode()
                        : role.getRoleName())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .map(code -> code.startsWith("ROLE_") ? code.substring(5) : code)
                .map(code -> code.toUpperCase(Locale.ROOT))
                .distinct()
                .toList();

        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponse(token, user.getUsername(), user.getEmail(), user.getFullName(), roles, user.getCreatedAt());
    }
}
