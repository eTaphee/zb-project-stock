package kr.co.zerobase.stock.persist.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kr.co.zerobase.stock.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "COMPANY")
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@ToString
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String name;

    public static CompanyEntity from(Company company) {
        return CompanyEntity.builder()
            .ticker(company.getTicker())
            .name(company.getName())
            .build();
    }
}
