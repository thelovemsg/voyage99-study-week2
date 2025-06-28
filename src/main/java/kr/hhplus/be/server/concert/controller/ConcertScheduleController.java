package kr.hhplus.be.server.concert.controller;

import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleCreateDto;
import kr.hhplus.be.server.concert.controller.dto.ConcertScheduleInfoDto;
import kr.hhplus.be.server.concert.service.ConcertScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concert/schedule")
@RequiredArgsConstructor
public class ConcertScheduleController {

    private final ConcertScheduleService concertScheduleService;

    @GetMapping
    public ResponseEntity<ConcertScheduleInfoDto.Response> getConcertScheduleInfo(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(concertScheduleService.getConcertScheduleInfo(id));
    }

    @PostMapping
    public ResponseEntity<ConcertScheduleCreateDto.Response> createConcertSchedule(@RequestBody ConcertScheduleCreateDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(concertScheduleService.createConcertSchedule(request));
    }
}
