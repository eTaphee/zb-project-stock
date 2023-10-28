package kr.co.zerobase.stock.model;

import static lombok.AccessLevel.PROTECTED;

import kr.co.zerobase.stock.persist.entity.CompanyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Company {

    private String ticker;
    private String name;

    public static Company fromEntity(CompanyEntity company) {
        return Company.builder()
            .ticker(company.getTicker())
            .name(company.getName())
            .build();
    }
}
