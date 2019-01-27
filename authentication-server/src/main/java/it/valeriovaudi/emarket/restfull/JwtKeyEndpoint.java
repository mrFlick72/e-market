package it.valeriovaudi.emarket.restfull;

import it.valeriovaudi.emarket.security.SecurityOAuth2AutorizationServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sign-key")
public class JwtKeyEndpoint {

    @GetMapping("/public")
    public ResponseEntity key() {
        return ResponseEntity.ok(SecurityOAuth2AutorizationServerConfig.KEY);
    }
}
