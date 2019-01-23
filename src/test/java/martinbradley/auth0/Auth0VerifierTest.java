package martinbradley.auth0;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

import martinbradley.auth0.Auth0Verifier;
import java.util.StringJoiner;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import martinbradley.security.JWTString;
import martinbradley.security.JWTSigner;
import martinbradley.security.RSASigner;

public class Auth0VerifierTest {
    private static final Logger logger = LoggerFactory.getLogger(JWTSigner.class);
    final KeyPair keyPair;
    final String issuer = "https://myeducation.eu.auth0.com/";
    JWTString.Builder builder;

    Auth0VerifierTest() throws Exception {
        keyPair = new RSASigner().getKeyPair();
    }

    @BeforeEach
    public void beforeAll() {
        createBuilder();
    }

    /* Creates a build tht by default will build
     * a valid JWT token */
    private JWTString.Builder createBuilder() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBefore = now.minusHours(1);
        LocalDateTime hourAfter  = now.plusHours(1);

        long issuedAt = toSinceEpoch(hourBefore);
        long expires  = toSinceEpoch(hourAfter);
        System.out.println("Issued at " + issuedAt);

        builder = new JWTString.Builder();
        builder.setIssuer(issuer)
               .setIat(issuedAt)
               .setExp(expires)
               .setScope("openid profile email read:patients");
        return builder;
    }

    private String createExpiredJWT() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBefore = now.minusHours(100);
        LocalDateTime hourAfter  = now.minusHours(1);// finished one hour ago!

        long issuedAt = toSinceEpoch(hourBefore);
        long expires  = toSinceEpoch(hourAfter);

        logger.warn("Issued at " + issuedAt);
        builder.setIat(issuedAt) 
               .setExp(expires);

        return createJWT();
    }

    private String createJWT() throws Exception {

        JWTString jwtString = builder.build();
        Object[] header = jwtString.getHeader();
        Object[] payload = jwtString.getPayload();

        JWTSigner signer = new JWTSigner(keyPair);
        signer.setHeader(header);
        signer.setPayload(payload);

        String jwt = signer.createSignedJWT();
        return jwt;
    }

    private String createValidJWT() throws Exception {

        return createJWT();
    }

    private long toSinceEpoch(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    @Test
    public void testExpiredJwt() throws Exception {
        String validJWT = createExpiredJWT();

        RSAPublicKey pub = (RSAPublicKey)keyPair.getPublic();

        Auth0Verifier auth = new Auth0Verifier(issuer, pub);
        boolean isValid = auth.validTokenHasScopes(validJWT, "read:patients");
        assertThat(isValid, is(false));
    }

    @Test
    public void testWrongIssuerJwt() throws Exception {
        String otherIssuer = "SomeOtherIssuer";

        validate(otherIssuer, false, "read:patients");
    }

    @Test
    public void testJwtBlankScope() throws Exception {
        validate(issuer, false, "");
    }

    @Test
    public void testValidJwtNoScopes() throws Exception {
        validate(issuer, false);
    }
    @Test
    public void testValidJwt() throws Exception {

        validate(issuer, true, "read:patients");
    }

    @Test
    public void testValidJwtTwoScopes() throws Exception {

        validate(issuer, true, "openid", "read:patients");
    }

    private void validate(String aIssuer, 
                          boolean expectedResult,
                          String ...aScopes) 
        throws Exception {

        String validJWT = createValidJWT();

        RSAPublicKey pub = (RSAPublicKey)keyPair.getPublic();

        Auth0Verifier auth = new Auth0Verifier(aIssuer, pub);
        boolean isValid = auth.validTokenHasScopes(validJWT, aScopes);
        assertThat(isValid, is(expectedResult));
    }
}
