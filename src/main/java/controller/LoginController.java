package controller;

import exception.AuthException;
import model.LoginRequest;
import model.RestErrorResponse;
import repository.DatabaseInfo;
import util.Constants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Base64;

@Path("/login")
public class LoginController {

    DatabaseInfo databaseInfo = new DatabaseInfo();

    @Path("/auth")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object login(LoginRequest loginRequest,
                        @HeaderParam("Authorization") String authString) {
        try {
            if (!isUserAuthenticated(authString)) {
                throw new AuthException(401, "Unauthorized");
            }
            return databaseInfo.authUser(loginRequest.getUsername(), loginRequest.getPassword());
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
            if (credentials[0].equals(Constants.BASIC_AUTH_USER) &&
                    credentials[1].equals(Constants.BASIC_AUTH_PATH)) {
                return true;
            }
        } else {
            throw new AuthException(402, Constants.BASIC_AUTH + " not found");
        }
        return false;
    }


}
