package kr.hhplus.be.server.ticket.infrastructure.persistence.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {
}
