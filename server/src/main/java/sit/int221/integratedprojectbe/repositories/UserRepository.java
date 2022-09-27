package sit.int221.integratedprojectbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.integratedprojectbe.entities.Event;
import sit.int221.integratedprojectbe.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByUserIdDesc();

    public boolean existsByNameAndUserIdNot(String Name, Integer userId);

    public boolean existsByEmailAndUserIdNot(String Email, Integer userId);

    public boolean existsByEmail(String Email);

    public boolean existsByName(String Name);

    User findByEmail (String email);



}