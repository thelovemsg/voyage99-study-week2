package kr.hhplus.be.server.concert.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import kr.hhplus.be.server.common.jpa.BaseEntity;
import kr.hhplus.be.server.concert.enums.ConcertStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "concert")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "concert_id")
    private Long concertId;
    
    @Column(name = "concert_name", length = 200)
    private String concertName;

    @Column(name = "artist_name", length = 100)
    private String artistName;

    @Column(name = "description", length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ConcertStatus status = ConcertStatus.STOPPED;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public boolean isConcertAvailable() {
        if(ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate))
            return false;

        if(this.status == ConcertStatus.STOPPED || this.status == ConcertStatus.READY)
            return false;

        return true;
    }

    public void openConcert() {
        this.status = ConcertStatus.ON_SELLING;
    }

}
