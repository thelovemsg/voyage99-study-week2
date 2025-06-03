package kr.hhplus.be.server.concert.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import kr.hhplus.be.server.common.jpa.BaseEntity;
import kr.hhplus.be.server.concert.enums.CommonStatusEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "concert_schedule")
public class ConcertScheduleEntity extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @Column(name = "venue_id", nullable = false, updatable = false)
    private Long venueId;

    @Column(name = "concert_date")
    private LocalDate concertDate;

    @Column(name = "concert_time")
    private LocalTime concertTime;

    @Column(name = "total_seats_number")
    private Integer totalSeatsNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private CommonStatusEnum scheduleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", referencedColumnName = "concert_id", insertable = false, updatable = false
                    , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ConcertEntity concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", referencedColumnName = "venue_id", insertable = false, updatable = false
            , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private VenueEntity venue;

}
