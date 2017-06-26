package com.jazasoft.mt.repository.tenant;

import com.jazasoft.mt.entity.tenant.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
