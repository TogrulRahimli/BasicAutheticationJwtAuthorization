package service;

import model.User;
import repository.DatabaseInfo;

import java.util.List;

public class UserService {

    private DatabaseInfo databaseInfo;

    public UserService(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public List<User> getUsers(){
        return databaseInfo.getUsers();
    }
}
