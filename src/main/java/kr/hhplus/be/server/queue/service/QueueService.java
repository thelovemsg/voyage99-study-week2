package kr.hhplus.be.server.queue.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.common.utils.CryptoUtils;
import kr.hhplus.be.server.queue.controller.dto.IssueTokenCommandDto;
import kr.hhplus.be.server.queue.controller.dto.QueueCreateCommandDto;
import kr.hhplus.be.server.queue.controller.dto.TokenValidateCommandDto;
import kr.hhplus.be.server.queue.domain.QueueEntity;
import kr.hhplus.be.server.queue.enums.QueueStatus;
import kr.hhplus.be.server.queue.enums.TokenStatus;
import kr.hhplus.be.server.queue.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private final CryptoUtils cryptoUtils;

    /**
     * 큐 생성
     */
    @Transactional
    public QueueCreateCommandDto.Response enterQueue(QueueCreateCommandDto.Request request) {
        // 이미 큐가 있는지 확인
        Long userId = request.getUserId();
        Long concertScheduleId = request.getConcertScheduleId();

        Optional<QueueEntity> existingQueue = queueRepository.findByUserIdAndConcertScheduleId(userId, concertScheduleId);

        if (existingQueue.isPresent()) {
            QueueEntity queue = existingQueue.get();
            return QueueCreateCommandDto.Response.builder()
                    .queueId(queue.getQueueId())
                    .userId(queue.getUserId())
                    .status(queue.getStatus().name())
                    .message(MessageCode.QUEUE_ALREADY_EXISTS.getMessage())
                    .createdAt(queue.getCreatedAt())
                    .build();
        }

        // 현재 대기열 위치 계산
        int currentPosition = queueRepository.countByStatusAndConcertScheduleId(QueueStatus.WAITING, concertScheduleId) + 1;

        String rawToken = cryptoUtils.generateSecureToken();
        String encryptedToken = cryptoUtils.encrypt(rawToken);

        // 새 큐 생성 (정적 팩토리 메서드 사용)
        QueueEntity queue = QueueEntity.createQueue(userId, concertScheduleId, currentPosition, encryptedToken);

        QueueEntity savedQueue = queueRepository.save(queue);

        log.info("사용자 {}의 큐가 생성되었습니다. Queue ID: {} / Concert schedule ID : {}", userId, savedQueue.getUserId(), concertScheduleId);

        return QueueCreateCommandDto.Response.builder()
                .queueId(savedQueue.getQueueId())
                .userId(savedQueue.getUserId())
                .status(savedQueue.getStatus().name())
                .message(MessageCode.QUEUE_CREATE_SUCCESS.getMessage())
                .createdAt(savedQueue.getCreatedAt())
                .build();
    }

    /**
     * 토큰 발급
     */
    @Transactional
    public IssueTokenCommandDto.Response issueToken(IssueTokenCommandDto.Request request) {
        // 사용자의 큐 찾기
        Optional<QueueEntity> queueOpt = queueRepository.findByUserIdAndConcertScheduleId(request.getUserId(), request.getConcertScheduleId());

        if (queueOpt.isEmpty()) {
            return IssueTokenCommandDto.Response.builder()
                    .success(false)
                    .message(MessageCode.QUEUE_NOT_FOUND.getMessage())
                    .build();
        }

        QueueEntity queue = queueOpt.get();

        // 이미 토큰이 발급되어 있는지 확인
        if (queue.getStatus() == QueueStatus.ACTIVE && !queue.isTokenExpired()) {
            return IssueTokenCommandDto.Response.builder()
                    .success(false)
                    .message(MessageCode.QUEUE_ALREADY_EXISTS.getMessage())
                    .expiresAt(queue.getExpiresAt())
                    .build();
        }

        // 토큰 생성 및 암호화
        String rawToken = cryptoUtils.generateSecureToken();
        String encryptedToken = cryptoUtils.encrypt(rawToken);

        // 큐에 토큰 저장
        queue.issueToken(encryptedToken);
        queueRepository.save(queue);

        log.info("사용자 {}에게 토큰이 발급되었습니다. Queue ID: {}", request.getUserId(), queue.getQueueId());

        return IssueTokenCommandDto.Response.builder()
                .token(rawToken) // 사용자에게는 원본 토큰 반환
                .expiresAt(queue.getExpiresAt())
                .message("토큰이 발급되었습니다.")
                .success(true)
                .build();
    }

    /**
     * 토큰 검증
     */
    @Transactional
    public TokenValidateCommandDto.Response validateToken(TokenValidateCommandDto.Request request) {
        // 토큰 암호화
        String encryptedToken = cryptoUtils.encrypt(request.getToken());

        // 사용자 ID와 암호화된 토큰으로 큐 찾기
        Optional<QueueEntity> queueOpt = queueRepository.findByUserIdAndConcertScheduleIdAndStatus(
                            request.getUserId()
                            , request.getConcertScheduleId()
                            , QueueStatus.ACTIVE);

        if (queueOpt.isEmpty()) {
            return TokenValidateCommandDto.Response.builder()
                    .isValid(false)
                    .status("NOT_FOUND")
                    .message("유효한 큐를 찾을 수 없습니다.")
                    .build();
        }

        QueueEntity queue = queueOpt.get();

        // 토큰 비교
        if (!encryptedToken.equals(queue.getEncryptedToken())) {
            return TokenValidateCommandDto.Response.builder()
                    .isValid(false)
                    .status(TokenStatus.INVALID.name())
                    .message("토큰이 일치하지 않습니다.")
                    .build();
        }

        // 만료 확인
        if (queue.isTokenExpired()) {
            queue.expireToken();
            queueRepository.save(queue);

            return TokenValidateCommandDto.Response.builder()
                    .isValid(false)
                    .status("EXPIRED")
                    .message("토큰이 만료되었습니다.")
                    .build();
        }

        log.info("사용자 {}의 토큰 검증 성공", request.getUserId());

        return TokenValidateCommandDto.Response.builder()
                .isValid(true)
                .status(TokenStatus.VALID.name())
                .message("유효한 토큰입니다.")
                .expiresAt(queue.getExpiresAt())
                .build();
    }
}