package kr.co.zerobase.stock.persist.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import kr.co.zerobase.stock.model.Dividend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "DIVIDEND")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"companyId", "date"})})
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@ToString
public class DividendEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public static DividendEntity of(Long companyId, Dividend dividend) {
        return DividendEntity.builder()
            .companyId(companyId)
            .date(dividend.getDate())
            .dividend(dividend.getDividend())
            .build();
    }
}
