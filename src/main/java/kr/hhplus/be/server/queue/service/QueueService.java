package kr.hhplus.be.server.queue.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.common.utils.CryptoUtils;
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
        Long userId = request.getUserId();
        Long concertScheduleId = request.getConcertScheduleId();
        String userToken = null;

        Optional<QueueEntity> existingQueue = queueRepository.findByUserIdAndConcertScheduleId(userId, concertScheduleId);

        if (existingQueue.isPresent()) {
            QueueEntity queue = existingQueue.get();
            String message = MessageCode.QUEUE_ALREADY_EXISTS.getMessage();
            return QueueCreateCommandDto.Response.fromEntity(queue, userToken, message);
        }

        // 현재 대기열 위치 계산
        int currentPosition = queueRepository.countByStatusAndConcertScheduleId(QueueStatus.WAITING, concertScheduleId) + 1;

        userToken  = cryptoUtils.generateSecureToken();
        String encryptedToken = cryptoUtils.encrypt(userToken);

        QueueEntity queue = QueueEntity.createQueue(userId, concertScheduleId, currentPosition, encryptedToken);

        QueueEntity savedQueue = queueRepository.save(queue);

        log.info("사용자 {}의 큐가 생성되었습니다. Queue ID: {} / Concert schedule ID : {}", userId, savedQueue.getUserId(), concertScheduleId);

        String resultMessage = MessageCode.QUEUE_CREATE_SUCCESS.getMessage();
        return QueueCreateCommandDto.Response.fromEntity(queue, userToken, resultMessage);
    }

    /**
     * 토큰 검증
     */
    @Transactional
    public TokenValidateCommandDto.Response validateToken(TokenValidateCommandDto.Request request) {
        // 토큰 암호화
        String encryptedToken = cryptoUtils.encrypt(request.getToken());

        Optional<QueueEntity> foundQueue = queueRepository.findByUserIdAndConcertScheduleIdAndEncryptedToken(
                request.getUserId(),
                request.getConcertScheduleId(),
                encryptedToken
        );

        if (foundQueue.isEmpty()) {
            return TokenValidateCommandDto.Response.builder()
                    .isValid(Boolean.FALSE)
                    .status(TokenStatus.INVALID.name())
                    .message(MessageCode.TOKEN_IS_NOT_VALID.getMessage())
                    .build();
        }

        QueueEntity queue = foundQueue.get();

        // 만료 확인
        if (queue.isTokenExpired()) {
            queue.expireToken();
            queueRepository.save(queue);

            return TokenValidateCommandDto.Response.builder()
                    .isValid(Boolean.FALSE)
                    .status(TokenStatus.EXPIRED.name())
                    .message(MessageCode.TOKEN_EXPIRED.format(queue.getExpiresAt()))
                    .build();
        }

        log.info("사용자 {}의 토큰 검증 성공", request.getUserId());

        return TokenValidateCommandDto.Response.builder()
                .isValid(Boolean.TRUE)
                .status(TokenStatus.VALID.name())
                .message(MessageCode.TOKEN_VALID.getMessage())
                .expiresAt(queue.getExpiresAt())
                .build();
    }
}