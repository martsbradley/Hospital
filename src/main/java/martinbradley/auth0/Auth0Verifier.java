package martsbradley.auth0;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Auth0Verifier {
    private final JWTVerifier verifier;
    private static Logger logger = LoggerFactory.getLogger(Auth0Verifier.class);

    public Auth0Verifier(String aIssuer,
                         RSAPublicKey aPublicKey) {

        RSAPrivateKey privateKey = null;// Not needed, here we only verify tokens.

        final Algorithm algorithm = Algorithm.RSA256(aPublicKey, privateKey);

        verifier = JWT.require(algorithm)
                      .withIssuer(aIssuer)
                      .build(); 
    }

    public boolean isTokenValid(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            logger.warn("Decoded successfully");
            return true;
        } catch (JWTVerificationException exception){
            logger.warn("Hit a problem, ", exception);
        }
        return false;
    }
}
