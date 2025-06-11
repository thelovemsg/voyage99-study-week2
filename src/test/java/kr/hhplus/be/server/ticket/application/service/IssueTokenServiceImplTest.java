package kr.hhplus.be.server.ticket.application.service;

import kr.hhplus.be.server.common.utils.CryptoUtils;
import kr.hhplus.be.server.common.utils.IdUtils;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.ticket.application.port.token.in.dto.IssueTokenCommandDto;
import kr.hhplus.be.server.ticket.application.service.token.IssueTokenServiceImpl;
import kr.hhplus.be.server.ticket.domain.model.Token;
import kr.hhplus.be.server.ticket.infrastructure.persistence.token.TokenRepositoryAdapter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class IssueTokenServiceImplTest {

    @Mock
    TokenRepositoryAdapter ticketRepositoryAdapter;

    @Mock
    CryptoUtils cryptoUtils;

    @InjectMocks
    IssueTokenServiceImpl service;

    private Long userId;
    private Long queueId;
    private Long concertScheduleId;
    private ConcertScheduleEntity mockConcertSchedule;

    @BeforeEach
    void setup() {
        this.userId = IdUtils.getNewId();
        this.queueId = IdUtils.getNewId();
        this.concertScheduleId = IdUtils.getNewId();

//        mockConcertSchedule = ConcertScheduleEntity.builder()
//                .concertScheduleId(concertScheduleId)
//                .concertId(IdUtils.getNewId())
//                .venueId(IdUtils.getNewId())
//                .concertDate(LocalDate.now())
//                .concertStartTime(LocalTime.of(2,2,2))
//                .concertEndTime(LocalTime.of(3,3,3))
//                .totalSeatsNumber(100)
//                .scheduleStatus(CommonStatusEnum.READY)
//                .build();

    }

    @Test
    @DisplayName("토큰 발행")
    void issueToken() {
        //given
        IssueTokenCommandDto.Request request = new IssueTokenCommandDto.Request();
        request.setUserId(this.userId);
        request.setQueueId(this.queueId);
        request.setConcertScheduleId(this.concertScheduleId);

        final String rawTokenId = "SECURE_TOKEN_TEST";
        final String encryptedData = "ENCRYPTED_DATA";

        IssueTokenCommandDto.Response response = new IssueTokenCommandDto.Response();

        //when
        Token token = Token.issueNewToken(request.getUserId(),
                request.getQueueId(),
                request.getConcertScheduleId(),
                "test");

        Mockito.when(cryptoUtils.generateSecureToken()).thenReturn(rawTokenId);
        Mockito.when(cryptoUtils.encrypt(rawTokenId)).thenReturn(encryptedData);
        Mockito.doNothing().when(ticketRepositoryAdapter).save(any(Token.class));

        //then

        IssueTokenCommandDto.Response issueTokenResponse = service.issueToken(request);

        Assertions.assertThat(issueTokenResponse.getRawTokenId()).isNotNull();
        Assertions.assertThat(issueTokenResponse.getRawTokenId()).isEqualTo(rawTokenId);
    }

    @Test
    @DisplayName("토큰 검증")
    void validateToken() {
        //given
        IssueTokenCommandDto.Request request = new IssueTokenCommandDto.Request();
        request.setUserId(this.userId);
        request.setQueueId(this.queueId);
        request.setConcertScheduleId(this.concertScheduleId);

        final String rawTokenId = "SECURE_TOKEN_TEST";
        final String encryptedData = "ENCRYPTED_DATA";

        IssueTokenCommandDto.Response response = new IssueTokenCommandDto.Response();

        //when
        Token token = Token.issueNewToken(request.getUserId(),
                request.getQueueId(),
                request.getConcertScheduleId(),
                "test");

        Mockito.when(cryptoUtils.generateSecureToken()).thenReturn(rawTokenId);
        Mockito.when(cryptoUtils.encrypt(rawTokenId)).thenReturn(encryptedData);
        Mockito.doNothing().when(ticketRepositoryAdapter).save(any(Token.class));

        //then

        IssueTokenCommandDto.Response issueTokenResponse = service.issueToken(request);

        Assertions.assertThat(issueTokenResponse.getRawTokenId()).isNotNull();
        Assertions.assertThat(issueTokenResponse.getRawTokenId()).isEqualTo(rawTokenId);
    }

}
