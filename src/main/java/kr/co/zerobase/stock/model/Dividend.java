package kr.co.zerobase.stock.model;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
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
}
