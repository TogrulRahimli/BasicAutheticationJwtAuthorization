package controller;

import model.LoginRequest;
import model.RefreshRequest;
import model.User;
import repository.DatabaseInfo;
import service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/login")
public class LoginController {

    private LoginService loginService;

    @Context
    private HttpServletRequest request;

    public LoginController() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        loginService = new LoginService(databaseInfo);
    }

    @Path("/auth")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object login(LoginRequest loginRequest,
                        @HeaderParam("Authorization") String authString) {
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), authString);
    }

    @Path("/refresh")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object refresh(RefreshRequest refreshRequest) {
        User currentUser = (User) request.getAttribute("user");
        return loginService.refresh(currentUser, refreshRequest.getRefreshToken());
    }

}
