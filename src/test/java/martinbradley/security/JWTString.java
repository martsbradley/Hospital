package martinbradley.security;

public class JWTString {
    private final String issuer;
    private final long iat;
    private final long exp;
    private final String scope;

    private JWTString(String aIssuer,
                      long aIat,
                      long aExp,
                      String aScope) {
        issuer = aIssuer;
        iat    = aIat;
        exp    = aExp;
        scope  = aScope;
    }

    public Object[] getHeader() {
        Object[] header = {"alg","RS256", 
                           "typ", "JWT"};
        return header;
    }
    public Object[] getPayload() {
        Object[] payload = {"sub","1234567890",
                            "name", "Martin Bradley",
                            "iss", issuer,
                            "scope", scope,
                            "iat", new Long(iat),
                            "exp", new Long(exp)}; 
        return payload;
    }

    public static class Builder {
        String issuer;
        long iat;
        long exp;
        String scope;

        public Builder setIssuer(String aIssuer) {
            this.issuer = aIssuer;
            return this;
        }
        public Builder setIat(long aIat) {
            this.iat = aIat;
            return this;
        }
        public Builder setExp(long aExp){
            this.exp = aExp;
            return this;
        }
        public Builder setScope(String aScope){
            this.scope = aScope;
            return this;
        }
        public JWTString build(){
            return new JWTString(issuer, iat, exp, scope);
        }
    }
}
