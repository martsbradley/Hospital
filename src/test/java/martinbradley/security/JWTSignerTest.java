package martsbradley.security;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

import martsbradley.auth0.Auth0Verifier;
import java.util.StringJoiner;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;


public class JWTSignerTest {
    private static final Logger logger = LoggerFactory.getLogger(JWTSigner.class);
    @Test
    public void call() throws Exception {
        logger.info("JWTSignerTest starting");
        JWTSigner signer = new JWTSigner();

        String issuer = "https://myeducation.eu.auth0.com/";

        String header  = new Joiner().create("alg","RS256", 
                                             "typ", "JWT");

        String payload = new Joiner().create("sub","1234567890",
                                             "name", "Martin Bradley",
                                             "iss", issuer,
                                             "iat", new Integer(1516239022));
        String jwt = signer.doit(header, payload);

        logger.info("header is  "+ header);
        logger.info("payload is  "+ payload);

        logger.info("Result is "+ jwt);

        RSASigner rsa = new RSASigner();

        KeyPair pair = rsa.getKeyPair();
        RSAPublicKey pub = (RSAPublicKey)pair.getPublic();

        Auth0Verifier auth = new Auth0Verifier(issuer, pub);
        boolean isvalid = auth.isTokenValid(jwt);
    }

    private static class Joiner {
        StringJoiner joiner = new StringJoiner(",", "{", "}");

        public String create(Object ... args) {

            Iterator<Object> it = Arrays.asList(args).iterator();

            while (it.hasNext()) {
                String key = (String)it.next();
                Object value = it.next();

                String item = getJWTSource(key, value);
                joiner.add(item);
            }
            return joiner.toString();
        }

        private String getJWTSource(String key, Object value) {
            if (value instanceof String)
                return String.format("\"%s\":\"%s\"", key,value);
            else
                return String.format("\"%s\":%d", key, value);
        }
    }
}
