package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import model.TokenPair;
import model.TokenType;
import model.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {

    private static final String AUTHOR_ID = "uid";
    private static final String FULLNAME = "title";
    private static final String TOKEN_TYPE_KEY = "token_type";

    private static String SECRET_KEY = "hojjrbj6ln5mdnzeh9fikrfbamcy33puuwh3f8190jmf9vbv0ek2vym6tf74zjg0npj7d31xm6mzz7e6sv8bpvmjdcjoc2z0k8uzk02j03jfjl5scsgivh3p5yt9sa6k";
    private static final long ACCESS_TOKEN_VALIDITY_IN_SECONDS = 900;
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

    public User parseJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        int userId = Integer.parseInt(String.valueOf(claims.get(AUTHOR_ID)));

        User user = new User();
        user.setId(userId);

        if (claims.get(TOKEN_TYPE_KEY).equals(TokenType.ACCESS.name())) {
            user.setName(claims.get(FULLNAME).toString());
            user.setUsername(claims.getSubject());
        }
        return user;
    }

    public TokenPair createTokenPair(User user) {
        TokenPair tokenPair = new TokenPair();
        tokenPair.setAccessToken(createAccessToken(user));
        tokenPair.setRefreshToken(createRefreshToken(user));
        return tokenPair;
    }

    public void validateToken(String authToken) {
        Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
    }
}