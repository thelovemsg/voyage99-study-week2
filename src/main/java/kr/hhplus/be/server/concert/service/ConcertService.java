package kr.hhplus.be.server.concert.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.concert.domain.ConcertEntity;
import kr.hhplus.be.server.concert.dto.ConcertUpdateDto;
import kr.hhplus.be.server.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository repository;

    public ConcertEntity findById(Long concertId) {
        return repository.findById(concertId).orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_NOT_FOUND, concertId));
    }

    @Transactional
    public ConcertEntity createConcert(ConcertEntity newConcert) {
        return repository.save(newConcert);
    }

    @Transactional
    public Long updateConcertInfo(ConcertUpdateDto.Request request) {
        ConcertEntity concertEntity = repository.findById(request.getConcertId()).orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_NOT_FOUND, request.getConcertId()));
        concertEntity.updateInfo(request.getConcertName(), request.getDescription(), request.getArtistName());
        return concertEntity.getConcertId();
    }

    @Transactional
    public void deleteConcertInfo(long concertId) {
        ConcertEntity concertEntity = repository.findById(concertId).orElseThrow(() -> new NotFoundException(MessageCode.CONCERT_NOT_FOUND, concertId));
        repository.delete(concertEntity);
    }
}
