package kr.hhplus.be.server.concert.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleCreateDto;
import kr.hhplus.be.server.concert.repository.ConcertScheduleRepository;
import kr.hhplus.be.server.concert.service.validator.ConcertScheduleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertScheduleService {

    private final ConcertScheduleRepository concertScheduleRepository;
    private final ConcertScheduleValidator scheduleValidator;

    @Transactional
    public ConcertScheduleCreateDto.Response createConcertSchedule(ConcertScheduleCreateDto.Request request) {
        scheduleValidator.validate(request);

        ConcertScheduleEntity newSchedule = ConcertScheduleCreateDto.Request.toEntity(request);
        ConcertScheduleEntity savedSchedule = concertScheduleRepository.save(newSchedule);
        return ConcertScheduleCreateDto.Response.fromEntity(savedSchedule);
    }

}
