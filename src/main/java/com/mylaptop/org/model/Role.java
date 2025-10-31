package com.mylaptop.org.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private int id; // Example: 1 = ROLE_USER, 2 = ROLE_ADMIN

    @Column(unique = true, nullable = false)
    private String name; // ROLE_USER / ROLE_ADMIN

    public Role() {}

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 🔹 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
