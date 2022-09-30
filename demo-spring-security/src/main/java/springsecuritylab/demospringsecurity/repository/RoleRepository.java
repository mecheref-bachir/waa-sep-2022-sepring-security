package springsecuritylab.demospringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springsecuritylab.demospringsecurity.domain.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
