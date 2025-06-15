package kr.hhplus.be.server.concert.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleInfoDto;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleCreateDto;
import kr.hhplus.be.server.concert.repository.ConcertScheduleJpaRepository;
import kr.hhplus.be.server.concert.service.validator.ConcertScheduleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertScheduleService {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertScheduleValidator scheduleValidator;

    @Transactional
    public ConcertScheduleCreateDto.Response createConcertSchedule(ConcertScheduleCreateDto.Request request) {
        scheduleValidator.validate(request);

        ConcertScheduleEntity newSchedule = ConcertScheduleCreateDto.Request.toEntity(request);
        ConcertScheduleEntity savedSchedule = concertScheduleJpaRepository.save(newSchedule);
        return ConcertScheduleCreateDto.Response.fromEntity(savedSchedule);
    }

    public ConcertScheduleInfoDto.Response getConcertScheduleInfo(Long id) {
        ConcertScheduleEntity entity = concertScheduleJpaRepository.findById(id).orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_SCHEDULE_NOT_FOUND, id));
        return ConcertScheduleInfoDto.Response.fromEntity(entity);
    }
}
