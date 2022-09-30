package springsecuritylab.demospringsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsecuritylab.demospringsecurity.domain.Role;
import springsecuritylab.demospringsecurity.domain.User;
import springsecuritylab.demospringsecurity.repository.RoleRepository;
import springsecuritylab.demospringsecurity.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements  UserService, UserDetailsService {
private final PasswordEncoder passwordEncoder;
    private  final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=userRepository.findByUserName(username);
       if(user==null){
           log.error("user {} not found in the database", username);
           throw new UsernameNotFoundException("user not found in the database");
       }else{

           log.info("user found in the database: {}",username);
       }
    Collection<SimpleGrantedAuthority> authorities =new ArrayList<>();
       user.getRoles().forEach(role -> authorities.add( new SimpleGrantedAuthority(role.getName())));


       return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("saving a new user {} to the database", user.getUserName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       return  userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving a new role {} to the database", role.getName());
       return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        log.info("adding a new role {} to the user {}", roleName,userName);
        User user =userRepository.findByUserName(userName);
        Role role=roleRepository.findByName(roleName);
        user.getRoles().add(role);


    }

    @Override
    public User getUser(String userName) {
        log.info("fetching user {}", userName);
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> getUsers() {
        log.info("fetching all users");
        return userRepository.findAll();
    }


}
