package kr.hhplus.be.server.ticket.infrastructure.persistence.token;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {

    @Id
    @Tsid
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "queue_id", nullable = false)
    private Long queueId;

    @Column(name = "concert_schedule_id", nullable = false)
    private Long concertScheduleId;

    @Column(name = "encrypted_value", nullable = false, unique = true, length = 500)
    private String encryptedValue;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;

}