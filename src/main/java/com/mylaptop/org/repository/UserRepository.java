
package com.mylaptop.org.repository;

import com.mylaptop.org.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // ✅ fixed: correct property path for collection of roles
    List<User> findAllByRoles_Name(String roleName);
}
