package springsecuritylab.demospringsecurity.service;

import springsecuritylab.demospringsecurity.domain.Role;
import springsecuritylab.demospringsecurity.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole (Role role);
    void addRoleToUser(String userName, String roleName);
    User getUser(String userName);
    List<User> getUsers ();


}
