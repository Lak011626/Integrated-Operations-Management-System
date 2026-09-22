package com.SCM.IOMS;

import com.SCM.IOMS.dto.request.LoginRequest;
import com.SCM.IOMS.dto.response.AuthResponse;
import com.SCM.IOMS.entity.Role;
import com.SCM.IOMS.entity.User;
import com.SCM.IOMS.repository.UserRepository;
import com.SCM.IOMS.security.CustomUserDetails;
import com.SCM.IOMS.security.JwtTokenProvider;
import com.SCM.IOMS.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Test
    void login_shouldReturnTokenAndUserInfo_whenCredentialsAreValid() {
        UserRepository userRepository = mock(UserRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        Role role = new Role();
        role.setRoleCode("ADMIN");
        role.setRoleName("Administrator");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPasswordHash("$2a$10$mockedHash");
        user.setEmail("admin@ioms.com");
        user.setFullName("System Admin");
        user.setRoles(Set.of(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                "123456",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token-123");

        AuthServiceImpl service = new AuthServiceImpl(authenticationManager, userRepository, jwtTokenProvider);
        AuthResponse response = service.login(new LoginRequest("admin", "123456"));

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());
        assertEquals("admin", response.getUsername());
        assertEquals("admin@ioms.com", response.getEmail());
        assertEquals("System Admin", response.getFullName());
        assertTrue(response.getRoles().contains("ADMIN"));
    }

    @Test
    void customUserDetails_shouldNormalizeRoleCodeWithoutDuplicateRolePrefix() {
        Role role = new Role();
        role.setRoleCode("ROLE_ADMIN");

        User user = new User();
        user.setUsername("admin");
        user.setPasswordHash("encoded");
        user.setRoles(Set.of(role));

        CustomUserDetails details = new CustomUserDetails(user);

        assertTrue(details.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }
}
