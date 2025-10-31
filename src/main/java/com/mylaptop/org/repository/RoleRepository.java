package com.mylaptop.org.repository;

import com.mylaptop.org.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

//    Optional<Role> findByCode(Integer code);
    Optional<Role> findByName(String name);
}
