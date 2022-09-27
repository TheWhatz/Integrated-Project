package sit.int221.integratedprojectbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.integratedprojectbe.entities.User;
import sit.int221.integratedprojectbe.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
}
