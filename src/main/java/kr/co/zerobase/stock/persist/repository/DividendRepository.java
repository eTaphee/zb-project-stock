package kr.co.zerobase.stock.persist.repository;

import java.time.LocalDateTime;
import java.util.List;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findAllByCompanyId(Long companyId);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);

    void deleteAllByCompanyId(Long companyId);
}
