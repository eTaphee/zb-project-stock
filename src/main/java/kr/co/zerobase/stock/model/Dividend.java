package kr.co.zerobase.stock.model;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import kr.co.zerobase.stock.persist.entity.DividendEntity;
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
public class Dividend {

    private LocalDateTime date;
    private String dividend;

    public static Dividend fromEntity(DividendEntity dividend) {
        return Dividend.builder()
            .date(dividend.getDate())
            .dividend(dividend.getDividend())
            .build();
    }
}
