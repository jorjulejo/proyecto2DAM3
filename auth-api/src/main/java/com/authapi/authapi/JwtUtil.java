package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // Hacerla no estática

    public String extractUsername(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, JWTClaimsSet::getExpirationTime);
    }

    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return claimsResolver.apply(claimsSet);
        } catch (Exception e) {
            return null; // Manejar cualquier excepción si el token no es válido
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    public String generateToken(Usuarios usuario) { // Cambiar UserDetails a Usuarios
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, usuario.getEmail()); // Usar el email como sujeto
    }

    private String createToken(Map<String, Object> claims, String subject) {
        try {
            long expirationTime = 1000 * 60 * 60 * 10; // 10 horas
            Date now = new Date();
            Date expiration = new Date(now.getTime() + expirationTime);

            // Crear el token JWT utilizando Nimbus JOSE + JJWT
            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256), // Algoritmo de firma (HS256 en este caso)
                new JWTClaimsSet.Builder()
                    .subject(subject) // Sujeto del token (email en este caso)
                    .expirationTime(expiration) // Tiempo de expiración
                    .issueTime(now) // Tiempo de emisión
                    .build()
            );

            // Firma el token con tu clave secreta
            signedJWT.sign(new MACSigner(secret));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            // Manejar excepción si ocurre un error al firmar el token
            e.printStackTrace(); // Imprimir el error en la consola para diagnóstico
            return null; // Devolver null en caso de error
        }
    }

    public Boolean validateToken(String token, Usuarios usuario) {
        final String username = extractUsername(token);
        return username != null && username.equals(usuario.getEmail()) && !isTokenExpired(token);
    }

    

}
