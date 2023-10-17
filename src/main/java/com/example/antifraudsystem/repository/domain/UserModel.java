package com.example.antifraudsystem.repository.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "application_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    public UserModel() {
    }

    public UserModel(String name, String username, String password, Role role, boolean isAccountNonLocked) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAccountNonLocked = isAccountNonLocked;
    }
}
