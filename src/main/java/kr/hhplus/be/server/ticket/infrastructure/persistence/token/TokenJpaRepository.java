package kr.hhplus.be.server.ticket.infrastructure.persistence.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByEncryptedValue(String encryptedValue);
}
