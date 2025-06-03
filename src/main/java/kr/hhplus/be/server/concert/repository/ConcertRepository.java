package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<ConcertEntity, Long> {
    ConcertEntity save(ConcertEntity concert);
}
