package ptit.edu.checkin.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ptit.edu.checkin.service.CustomUserDetails;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final  String JWT_SECRET="abcabc";
    private final long JWT_EXPIRATION=3600000L;
    public String generateToken(CustomUserDetails userDetails){
        Date now=new Date();
        Date expiryDate =new Date(now.getTime()+JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject((userDetails.getUser().getUserName()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public  String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
