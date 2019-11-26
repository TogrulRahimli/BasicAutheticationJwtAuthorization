package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import model.TokenPair;
import model.TokenType;
import model.User;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private static final String AUTHOR_ID = "uid";
    private static final String FULLNAME = "title";
    private static final String TOKEN_TYPE_KEY = "token_type";

    private static String SECRET_KEY = "hojjrbj6ln5mdnzeh9fikrfbamcy33puuwh3f8190jmf9vbv0ek2vym6tf74zjg0npj7d31xm6mzz7e6sv8bpvmjdcjoc2z0k8uzk02j03jfjl5scsgivh3p5yt9sa6k";
    private static final long ACCESS_TOKEN_VALIDITY_IN_SECONDS = 180;
    private static final long REFRESH_TOKEN_VALIDITY_IN_SECONDS = 120;

    private Key key;

    public JwtTokenProvider() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private String createAccessToken(User user) {

        long now = (new Date()).getTime();
        Date validity = new Date(now + ACCESS_TOKEN_VALIDITY_IN_SECONDS * 1000);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(FULLNAME, user.getName())
                .claim(AUTHOR_ID, user.getId())
                .claim(TOKEN_TYPE_KEY, TokenType.ACCESS)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }


    private String createRefreshToken(User user) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + REFRESH_TOKEN_VALIDITY_IN_SECONDS * 1000);

        return Jwts.builder()
                .claim(AUTHOR_ID, user.getId())
                .claim(TOKEN_TYPE_KEY, TokenType.REFRESH)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

//    public static String createJWT(User user) {
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//
//        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
//
//        Key signingKey = Keys.hmacShaKeyFor(keyBytes);
//
//        //Let's set the JWT Claims
//        JwtBuilder builder = Jwts.builder()
//                .setId(String.valueOf(user.getId()))
//                .setIssuedAt(now)
//                .setSubject(user.getUsername())
//                .setIssuer(user.getName())
//                .signWith(signingKey, SignatureAlgorithm.HS512);
//
//        //if it has been specified, let's add the expiration
//        long expMillis = nowMillis + EXPIRY_MILLIS * 1000;
//        Date exp = new Date(expMillis);
//        builder.setExpiration(exp);
//
//        return builder.compact();
//    }

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt)
                .getBody();
    }

    public TokenPair createTokenPair(User user) {
        TokenPair tokenPair = new TokenPair();
        tokenPair.setAccessToken(createAccessToken(user));
        tokenPair.setRefreshToken(createRefreshToken(user));
        return tokenPair;
    }

    public void validateToken(String authToken) {
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
    }
}