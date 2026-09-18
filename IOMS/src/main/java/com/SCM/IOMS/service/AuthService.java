package com.SCM.IOMS.service;

import com.SCM.IOMS.dto.request.LoginRequest;
import com.SCM.IOMS.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
