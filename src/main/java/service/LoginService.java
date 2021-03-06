package service;

import exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import model.RestErrorResponse;
import model.User;
import repository.DatabaseInfo;
import util.Constants;
import util.JwtTokenProvider;

import java.util.Base64;

public class LoginService {

    private DatabaseInfo databaseInfo;
    private JwtTokenProvider tokenProvider;

    public LoginService(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
        tokenProvider = new JwtTokenProvider();
    }

    public Object login(String username, String password, String authString) {
        try {
            if (!isUserAuthenticated(authString)) {
                throw new AuthException(401, "Unauthorized");
            }

            User user = databaseInfo.authUser(username, password);

            return tokenProvider.createTokenPair(user);
        } catch (AuthException ux) {
            return new RestErrorResponse(ux.getErrorCode(), ux.getErrorMessage());
        }
    }

    private boolean isUserAuthenticated(String authString) {
        if (authString.contains(Constants.BASIC_AUTH)) {
            String base64Credentials = authString.substring(Constants.BASIC_AUTH.length()).trim();
            String credentialsString = new String(Base64.getDecoder().decode(base64Credentials));
            String[] credentials = credentialsString.split(":");
            if (credentials.length != 2) {
                throw new AuthException(402, Constants.BASIC_AUTH + " not found");
            }
            return credentials[0].equals(Constants.BASIC_AUTH_USER) &&
                    credentials[1].equals(Constants.BASIC_AUTH_PATH);
        } else {
            throw new AuthException(402, Constants.BASIC_AUTH + " not found");
        }
    }

    public Object refresh(User currentUser, String refreshToken) {
        try {
            tokenProvider.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            return new RestErrorResponse(408, "Refresh token Expired");
        } catch (Exception e) {
            return new RestErrorResponse(401, "Unauthorized");
        }

        return tokenProvider.createTokenPair(currentUser);
    }
}
