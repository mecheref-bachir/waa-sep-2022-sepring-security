package springsecuritylab.demospringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springsecuritylab.demospringsecurity.domain.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);
}
