package kr.hhplus.be.server.concert.service.validator;

import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleCreateDto;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import kr.hhplus.be.server.concert.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class ConcertScheduleValidator {

    private final ConcertRepository concertRepository;
    private final VenueRepository venueRepository;

    public void validate(ConcertScheduleCreateDto.Request request) {
        Long concertId = request.getConcertId();
        Long venueId = request.getVenueId();

        concertRepository.findById(concertId).orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_NOT_FOUND, concertId));
        venueRepository.findById(venueId).orElseThrow(() -> new NotFoundException(MessageCode.VENUE_NOT_FOUND, venueId));

        validateConcertScheduleBusiness(request);
    }

    private void validateConcertScheduleBusiness(ConcertScheduleCreateDto.Request request) {

        validateConcertDuration(request.getConcertStartTime(), request.getConcertEndTime());
    }

    private void validateConcertDuration(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return;
        }

        if (endTime.isBefore(startTime)) {
            throw new ParameterNotValidException(MessageCode.INPUT_DATE_RANGE_NOT_PROPER_ERROR);
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.toMinutes() < 30) {
            throw new ParameterNotValidException(MessageCode.CONCERT_PLAY_TIME_NOT_PROPER_ERROR, startTime, endTime);
        }
    }
}
