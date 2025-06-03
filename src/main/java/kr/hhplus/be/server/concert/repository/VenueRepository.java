package kr.hhplus.be.server.concert.repository;

import kr.hhplus.be.server.concert.domain.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
}
