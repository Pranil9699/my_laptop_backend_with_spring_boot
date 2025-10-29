package com.mylaptop.org.repository;

import com.mylaptop.org.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // JOIN query: return users that are linked to a role with the given name
    @Query("SELECT u FROM User u JOIN u.role r WHERE r.name = :roleName")
    List<User> findAllByRoleName(@Param("roleName") String roleName);
}
