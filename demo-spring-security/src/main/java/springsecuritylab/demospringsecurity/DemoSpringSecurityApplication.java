package springsecuritylab.demospringsecurity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springsecuritylab.demospringsecurity.domain.Role;
import springsecuritylab.demospringsecurity.domain.User;
import springsecuritylab.demospringsecurity.service.UserService;

import java.util.ArrayList;

@SpringBootApplication
public class DemoSpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringSecurityApplication.class, args);
	}
	@Bean
PasswordEncoder passwordEncoder(){

		return new BCryptPasswordEncoder();
}
@Bean
	CommandLineRunner run (UserService userService){
		return args -> {

            userService.saveRole(new Role(null,"USER_ROLE"));
			userService.saveRole(new Role(null,"USER_MANAGER"));
			userService.saveRole(new Role(null,"USER_ADMIN"));
			userService.saveRole(new Role(null,"USER_SUPER_ADMIN"));

			userService.saveUser(new User(null, "bachir", "bachir123", "123", new ArrayList<>()));
			userService.saveUser(new User(null, "mecheref", "mecheref123", "123", new ArrayList<>()));

			userService.addRoleToUser("bachir123","USER_ROLE");
			userService.addRoleToUser("bachir123","USER_MANAGER");

			userService.addRoleToUser("mecheref123","USER_MANAGER");
			userService.addRoleToUser("mecheref123","USER_ADMIN");
			userService.addRoleToUser("mecheref123","USER_SUPER_ADMIN");



		};

	}

}
