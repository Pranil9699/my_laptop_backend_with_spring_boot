package com.mylaptop.org.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;  // e.g. ROLE_USER / ROLE_ADMIN

    @Column(unique = true, nullable = false)
    private Integer code; // e.g. 501, 502

    // 🧱 Constructors
    public Role() {}

    public Role(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public Role(Long id, String name, Integer code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    // 🧱 Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", code=" + code + '}';
    }
}
