package com.mylaptop.org.service;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.mylaptop.org.model.Role;
import com.mylaptop.org.repository.RoleRepository;

@Service
public class DataInitializer {

    private final RoleRepository roleRepo;

    public DataInitializer(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @PostConstruct
    public void init() {
        if (roleRepo.count() == 0) {
        	Role userRole = new Role();
        	userRole.setId(501);
        	userRole.setName("ROLE_USER");
//        	userRole.setCode(501);

        	Role adminRole = new Role();
        	adminRole.setId(502);
        	adminRole.setName("ROLE_ADMIN");
//        	adminRole.setCode(502);

        	roleRepo.save(userRole);
        	roleRepo.save(adminRole);

            System.out.println("✅ Default roles inserted via @PostConstruct");
        }
    }
}
