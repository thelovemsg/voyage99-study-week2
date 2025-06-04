package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertScheduleRepository extends JpaRepository<ConcertScheduleEntity, Long> {
}
