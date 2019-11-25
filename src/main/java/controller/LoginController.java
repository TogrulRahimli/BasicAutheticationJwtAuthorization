package controller;

import exception.AuthException;
import model.LoginRequest;
import model.RestErrorResponse;
import repository.DatabaseInfo;
import service.LoginService;
import util.Constants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Base64;

@Path("/login")
public class LoginController {

    private DatabaseInfo databaseInfo = new DatabaseInfo();
    private LoginService loginService = new LoginService(databaseInfo);

    @Path("/auth")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object login(LoginRequest loginRequest,
                        @HeaderParam("Authentication") String authString) {
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), authString);
    }

}
