package com.training.store.auth.controllers;

import com.training.store.auth.configurations.JwtConfig;
import com.training.store.common.dtos.ErrorDto;
import com.training.store.auth.dtos.JwtResponse;
import com.training.store.auth.dtos.LoginRequest;
import com.training.store.users.dtos.UserDto;
import com.training.store.common.exceptions.ErrorType;
import com.training.store.common.filters.CorrelationIdFilter;
import com.training.store.users.mappers.UserMapper;
import com.training.store.users.repositories.UserRepository;
import com.training.store.auth.services.AuthService;
import com.training.store.auth.services.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse loginUser(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        var loginResult = authService.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();

        var cookie = new Cookie(jwtConfig.getRefreshTokenKey(), refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath(jwtConfig.getRefreshTokenCookiePath());
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {

        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {

        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> badCredentialsExceptionHandler() {
        var error = new ErrorDto(
                ErrorType.UNAUTHORIZED_ACCESS.getStatus().value(),
                ErrorType.UNAUTHORIZED_ACCESS.getCode(),
                ErrorType.UNAUTHORIZED_ACCESS.name(),
                "That didn’t work. Please double-check your login details/refresh-token and try again.",
                Instant.now(),
                MDC.get(CorrelationIdFilter.CORRELATION_ID)
        );
        return ResponseEntity.status(error.status()).body(error);
    }
}
