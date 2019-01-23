package martinbradley.security;
import java.util.StringJoiner;
import java.util.Arrays;

public class JWTString {
    private final String issuer;
    private final long iat;
    private final long exp;
    private final String scope;
    private final String namespace, groups;  

    private JWTString(Builder aBuilder) {
        issuer    = aBuilder.issuer;
        iat       = aBuilder.iat;
        exp       = aBuilder.exp;
        scope     = aBuilder.scope;
        namespace = aBuilder.namespace;
        groups    = aBuilder.groups;
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

        if (namespace != null && groups != null) {
            Object larger[] = Arrays.copyOf(payload, payload.length+2);
            larger[larger.length-2] = namespace;
            larger[larger.length-1] = "groups: [" + groups + "]";
            payload = larger;
        }
        return payload;
    }

    public static class Builder {
        String issuer;
        long iat;
        long exp;
        String scope;
        String namespace;
        String groups;

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
        public Builder setGroups(String namespace, String ... groups) {
            this.namespace = namespace;
            StringJoiner joiner = new StringJoiner(",");
            for (String group: groups) {
                joiner.add(group);
            }
            this.groups = joiner.toString();
            return this;
        }
        public JWTString build(){
            return new JWTString(this);
        }
    }
}
