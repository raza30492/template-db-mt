package com.jazasoft.mt.entity.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jazasoft.mt.entity.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Entity
public class Company extends BaseEntity implements Serializable{

    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @NotNull
    @Column(nullable = false)
    private String address;

    @NotNull
    @Column(nullable = false, unique = true)
    private String dbName;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private Set<Role> roles = new HashSet<>();

    public Company() {
    }

    public Company(String name, String description, String address, String dbName) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.dbName = dbName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
