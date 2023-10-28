package kr.co.zerobase.stock.persist.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "DIVIDEND")
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

}
