package com.jazasoft.mt.entity.master;

import com.jazasoft.mt.entity.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Entity
public class Company extends BaseEntity {

    private String name;

    private String description;

    private String address;

    @Column(name = "db_name")
    private String dbName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

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
}
