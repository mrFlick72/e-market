package it.valeriovaudi.emarket.restfull;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;

@Slf4j
@RestController
@RequestMapping("/sign-key")
public class JwtKeyEndpoint {

    @GetMapping("/public")
    public ResponseEntity key() throws JOSEException {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "secret".toCharArray());
        KeyPair emarket = keyStoreKeyFactory.getKeyPair("emarket");

        JWK jwk = new RSAKey.Builder((RSAPublicKey) emarket.getPublic())
                .privateKey((RSAPrivateKey) emarket.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .build();


        return ResponseEntity.ok(new Jwts(asList(jwk.toJSONObject())));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Jwts {
    private List<JSONObject> keys;
}