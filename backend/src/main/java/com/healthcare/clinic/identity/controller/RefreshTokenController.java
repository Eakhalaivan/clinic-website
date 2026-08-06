package com.healthcare.clinic.identity.controller;

import com.healthcare.clinic.identity.entity.RefreshToken;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.service.RefreshTokenService;
import com.healthcare.clinic.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        try {
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        
                        String token = jwtUtils.generateJwtToken(auth);
                        
                        // Rotate refresh token
                        refreshTokenService.deleteByUserId(user.getId());
                        String newRefreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
                        
                        return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken));
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody TokenRefreshRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken()).ifPresent(token -> {
            refreshTokenService.deleteByUserId(token.getUser().getId());
        });
        return ResponseEntity.ok("Log out successful");
    }
}

@Data
class TokenRefreshRequest {
    @jakarta.validation.constraints.NotBlank
    private String refreshToken;
}

@Data
class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
