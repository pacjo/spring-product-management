package nodomain.cloudfide.task.repository;

import nodomain.cloudfide.task.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE " +
        "(:producerId IS NULL OR p.producer.id = :producerId) AND " +
        "(:producerName IS NULL OR LOWER(p.producer.name) LIKE LOWER(CONCAT('%', :producerName, '%'))) AND " +
        "(:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
        "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
        "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> findByFilters(
        @Param("producerId") UUID producerId,
        @Param("producerName") String producerName,
        @Param("productName") String productName,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        Pageable pageable
    );

    Page<Product> findByProducerId(UUID producerId, Pageable pageable);

    long countByProducerId(UUID producerId);
}