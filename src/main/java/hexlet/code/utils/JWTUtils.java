package hexlet.code.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class JWTUtils {
    @Autowired
    private JwtEncoder jwtEncoder;

    public String generateToken(String email) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("AuthServer")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(email)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
