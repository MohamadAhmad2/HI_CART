package io.moeah.hicartapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.moeah.hicartapi.model.persistent.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByNameIgnoreCase(String name);

	@Query("select case when count(p)> 0 then true else false end from Product p where p.seller.id = :seller_id")
	Boolean existsBySellerId(@Param("seller_id") Long sellerId);
}
