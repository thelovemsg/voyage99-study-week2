package kr.hhplus.be.server.ticket.domain.service;

import kr.hhplus.be.server.common.exceptions.ParameterNotValidException;
import kr.hhplus.be.server.common.exceptions.ReservationNotValidException;
import kr.hhplus.be.server.common.messages.MessageCode;
import kr.hhplus.be.server.concert.domain.ConcertScheduleEntity;
import kr.hhplus.be.server.concert.enums.CommonStatusEnum;
import kr.hhplus.be.server.ticket.domain.model.Ticket;
import kr.hhplus.be.server.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TicketDomainService {

    public void validateTicketCanBeReserved(Ticket ticket, Long userId) {
        if (ticket.isReservedByOther(userId) && !ticket.isReservationExpired()) {
            throw new ParameterNotValidException(MessageCode.TICKET_ALREADY_OCCUPIED);
        }
    }

    public void validateUserHasEnoughPoint(UserEntity user, BigDecimal useAmount) {
        BigDecimal userPointAmount = user.getPointAmount();

        if(useAmount.compareTo(userPointAmount) < 0)
            throw new ParameterNotValidException(MessageCode.USER_POINT_NOT_ENOUGH, userPointAmount);

    }

    public void validateConcertScheduleAvailable(ConcertScheduleEntity schedule) {
        CommonStatusEnum scheduleStatus = schedule.getScheduleStatus();
        if(scheduleStatus != CommonStatusEnum.ON_SELLING) {
            throw new ReservationNotValidException(MessageCode.CONCERT_SCHEDULE_NOT_AVAILABLE);
        }
    }

}
