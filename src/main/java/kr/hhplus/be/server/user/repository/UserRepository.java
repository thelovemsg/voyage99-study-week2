package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
