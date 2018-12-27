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
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class JWTSignerTest {
    private static final Logger logger = LoggerFactory.getLogger(JWTSigner.class);
    final KeyPair keyPair;
    final String issuer = "https://myeducation.eu.auth0.com/";


    JWTSignerTest() throws Exception {
        keyPair = new RSASigner().getKeyPair();
    }

    private String createJWT(String issuer, 
                                  long iat,
                                  long exp)
        throws Exception {
        Object[] header = {"alg","RS256", 
                           "typ", "JWT"};

        Object[] payload = {"sub","1234567890",
                            "name", "Martin Bradley",
                            "iss", issuer,
                            "iat", new Long(iat),
                            "exp", new Long(exp)}; 

        JWTSigner signer = new JWTSigner(keyPair);
        signer.setHeader(header);
        signer.setPayload(payload);

        String jwt = signer.createSignedJWT();
        return jwt;
    }
    private long toSinceEpoch(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private String createValidJWT() 
        throws Exception {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBefore = now.minusHours(1);
        LocalDateTime hourAfter  = now.plusHours(1);

        long issuedAt = toSinceEpoch(hourBefore);
        long expires  = toSinceEpoch(hourAfter);
        System.out.println("Issued at " + issuedAt);

        return createJWT(issuer, issuedAt, expires);
    }

    private String createExpiredJWT() 
        throws Exception {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBefore = now.minusHours(100);
        LocalDateTime hourAfter  = now.minusHours(1);// finished one hour ago!

        long issuedAt = toSinceEpoch(hourBefore);
        long expires  = toSinceEpoch(hourAfter);
        System.out.println("Issued at " + issuedAt);

        return createJWT(issuer, issuedAt, expires);
    }

    @Test
    public void testValidJwt() throws Exception {
        String validJWT = createValidJWT();


        RSAPublicKey pub = (RSAPublicKey)keyPair.getPublic();

        Auth0Verifier auth = new Auth0Verifier(issuer, pub);
        boolean isValid = auth.isTokenValid(validJWT);
        assertThat(isValid, is(true));
    }

    @Test
    public void testExpiredJwt() throws Exception {
        String validJWT = createExpiredJWT();


        RSAPublicKey pub = (RSAPublicKey)keyPair.getPublic();

        Auth0Verifier auth = new Auth0Verifier(issuer, pub);
        boolean isValid = auth.isTokenValid(validJWT);
        assertThat(isValid, is(false));
    }
    @Test
    public void testWrongIssuerJwt() throws Exception {
        String validJWT = createValidJWT();

        RSAPublicKey pub = (RSAPublicKey)keyPair.getPublic();
        String otherIssuer = "SomeOtherIssuer";

        Auth0Verifier auth = new Auth0Verifier(otherIssuer, pub);
        boolean isValid = auth.isTokenValid(validJWT);
        assertThat(isValid, is(false));
    }
}
