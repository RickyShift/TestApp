package gpi.scrum.service;

import gpi.scrum.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${secret.key}") //Valor localizado no ficheiro /src/main/resources/application.properties
    private String SECRET_KEY;

    @Value("${token.durantion.time}") //Valor localizado no ficheiro /src/main/resources/application.properties
    private Integer DURATION;


    /*
    Retorna o Username a partir do token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
    Este token corresponde ao User correto e se o token ainda está válido
     */
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username != null && username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /*
    Função que verifica se o ‘token’ está expirado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*
    Função que retira o data em que o ‘token’ expira
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /*
    Função que resolve um determina claim a partir do 'claimsResolver'
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
    Função auxiliar que devolve todas as Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*
    Função que cria o ‘Token’
     */
    public String generateToken(User user) {
        return Jwts.
                builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() +  DURATION))
                .signWith(getSigningKey())
                .compact();
    }

    /*
    Função que cria a Key a partir da SECRET_KEY
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
