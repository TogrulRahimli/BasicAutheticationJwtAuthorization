package controller;

import model.User;
import repository.DatabaseInfo;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("")
public class UserController {

    private UserService userService;

    @Context
    private HttpServletRequest request;

    public UserController() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        userService = new UserService(databaseInfo);
    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Path("/personal/info")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getPersonalInfo() {
        //maybe you want get other personal info by current user info
        return (User) request.getAttribute("user");
    }


}
