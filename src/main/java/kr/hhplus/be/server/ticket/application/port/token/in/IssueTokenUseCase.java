package kr.hhplus.be.server.ticket.application.port.token.in;

import kr.hhplus.be.server.ticket.application.port.token.in.dto.IssueTokenCommandDto;

public interface IssueTokenUseCase {
    IssueTokenCommandDto.Response issueToken(IssueTokenCommandDto.Request request);
}
