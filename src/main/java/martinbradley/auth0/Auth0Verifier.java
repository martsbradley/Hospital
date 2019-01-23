package martinbradley.auth0;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.PublicKey;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import com.auth0.jwt.interfaces.Claim;
import static java.util.stream.Collectors.toSet;

public class Auth0Verifier {
    private final JWTVerifier verifier;
    private static Logger logger = LoggerFactory.getLogger(Auth0Verifier.class);

    public Auth0Verifier(String aIssuer,
                         PublicKey aPublicKey) {

        RSAPrivateKey privateKey = null;// Not needed, here we only verify tokens.

        final Algorithm algorithm = Algorithm.RSA256((RSAPublicKey)aPublicKey, 
                                                     privateKey);
        verifier = JWT.require(algorithm)
                      .withIssuer(aIssuer)
                      .build(); 
    }

    public boolean validTokenHasScopes(String token, String ... scopes) {
        try {
            if (scopes == null || scopes.length == 0) {
                logger.warn("A scope is mandatory");
                return false;
            }
            logger.debug("Checking token" + token);
            DecodedJWT jwt = verifier.verify(token);
            
            logger.debug("Decoded successfully");

            if (!jwtHasRequiredScope(jwt, scopes)) {
                return false;
            }

            return true;
        } catch (JWTVerificationException exception){
            logger.warn("JWT Not valid : "+ exception.getMessage());
        }
        return false;
    }

    private boolean jwtHasRequiredScope(DecodedJWT aJwt,
                                     String ... aRequiredScope) {

        Set<String> tokenScopes = scopesFromJwt(aJwt);

        List<String> required = Arrays.asList(aRequiredScope);

        boolean isValid = tokenScopes.containsAll(required);

        if (!isValid) {
            logger.warn("Missing claim scope.\ntokenScope " + tokenScopes);
            logger.warn("required " + required);
        }
        return isValid;
    }

    private Set<String> scopesFromJwt(DecodedJWT aJwt) {
        Set<String> scopes = new HashSet<>();

        Map<String, Claim> claims = aJwt.getClaims();
        Claim claim = claims.get("scope");

        if (claim != null) {
            String[] allowedScopes = claim.asString().split(" ");
            scopes = Arrays.stream(allowedScopes).collect(toSet());
        }
        return scopes;
    }
}
