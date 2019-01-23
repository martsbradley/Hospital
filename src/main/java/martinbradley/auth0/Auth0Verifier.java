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

    public boolean tokenIsValid(String token) {
        
        DecodedJWT result = getToken(token);
        return result != null;
    }
    /*
     *
  {
  "https://gorticrum.com/user_authorization": {
    "groups": [
      "adminGroup"
    ]
  },
  "iss": "https://myeducation.eu.auth0.com/",
  "sub": "auth0|5c1d5347b171c1019044870e",
  "aud": [
    "http://localhost:8080/firstcup/hospital/rest",
    "https://myeducation.eu.auth0.com/userinfo"
  ],
  "iat": 1548274059,
  "exp": 1a548277659,
  "azp": "eoK8GT2cFHcYWIbdGy-7qm9Wx5sanGkh",
  "scope": "openid profile email read:patients"
}
*/
    private Set<String> allowedGroups(DecodedJWT aJwt,
                                      String aNamespace) {
        Map<String, Claim> claims = aJwt.getClaims();

        Set<String> allowedGroups = new HashSet<>();

        if (claims.containsKey(aNamespace)) {
            Claim groupsClaim = claims.get(aNamespace);
            String includedGroups = groupsClaim.asString();
            logger.warn("includedGroups '" + includedGroups + "'");

            int startIdx = includedGroups.indexOf("[");
            int endIdx = includedGroups.indexOf("]");
            includedGroups = includedGroups.substring(startIdx+1, endIdx);

            logger.warn("includedGroups '" + includedGroups + "'");

            String[] allowedGroupsArr = includedGroups.split(",");

            for (String allowedGroup: allowedGroupsArr) {
                allowedGroups.add(allowedGroup);
            }
        }
        return allowedGroups;
    }

    public boolean isValidAccessRequest(String token, 
                                        String namespace,
                                        String ... aRequiredGroups) {
        DecodedJWT jwt = getToken(token);

        if (jwt == null ) {
            logger.warn("jwt is null");
            return false;
        }

        if (aRequiredGroups.length == 0) {
            logger.warn("Need to specify groups when using security");
            return false;
        }

        Set<String> allowedGroups = allowedGroups(jwt, namespace);


        Set<String> requiredGroups = new HashSet<>(
                                           Arrays.asList(aRequiredGroups));
        for (String group: allowedGroups) {
            logger.debug("Allowed group name '" +group + "'");
        }

        for (String group: requiredGroups) {
            logger.debug("Required group name '" +group + "'");
        }

        boolean isValid = allowedGroups.containsAll(requiredGroups);

        logger.info("isValidAccessRequest returning " + isValid);
        if (!isValid) {
            logger.warn("Authorization failed for " + token);
        }
        return isValid;
    }

    private DecodedJWT getToken(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            logger.warn("JWT Not valid : "+ exception.getMessage());
        }
        return jwt;
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
