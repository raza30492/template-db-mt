package com.jazasoft.mt.entity.tenant;

import com.jazasoft.mt.entity.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Entity
public class Product extends BaseEntity {

    @Audited
    private String name;

    @Audited
    private String description;

    public Product() {
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
}
