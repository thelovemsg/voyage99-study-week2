package kr.hhplus.be.server.user.domain;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Tsid
    @Column(name = "user_id")
    private Long userId;
}
