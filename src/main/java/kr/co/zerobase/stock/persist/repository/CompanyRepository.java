package kr.co.zerobase.stock.persist.repository;

import java.util.List;
import java.util.Optional;
import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name);

    Optional<CompanyEntity> findByTicker(String ticker);

    List<CompanyEntity> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
}
