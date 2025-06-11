package kr.hhplus.be.server.ticket.infrastructure.web;

import kr.hhplus.be.server.ticket.application.port.token.in.dto.IssueTokenCommandDto;
import kr.hhplus.be.server.ticket.application.port.token.in.dto.ValidateTokenCommandDto;
import kr.hhplus.be.server.ticket.application.service.token.IssueTokenServiceImpl;
import kr.hhplus.be.server.ticket.application.service.token.ValidateTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final IssueTokenServiceImpl issueTokenService;
    private final ValidateTokenServiceImpl validateTokenService;

    @PostMapping("/issue")
    public ResponseEntity<IssueTokenCommandDto.Response> issueToken(@RequestBody IssueTokenCommandDto.Request request) {
        IssueTokenCommandDto.Response response = issueTokenService.issueToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenCommandDto.Response> validateToken(@RequestBody ValidateTokenCommandDto.Request request) {
        Boolean result = validateTokenService.validateToken(request.getRawTokenId());
        return ResponseEntity.status(HttpStatus.OK).body(new ValidateTokenCommandDto.Response(result));
    }

}
