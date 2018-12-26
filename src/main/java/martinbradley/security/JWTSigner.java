package martsbradley.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;
import static java.util.Base64.Encoder;
import static java.util.Base64.Decoder;
import java.security.KeyPair;
import java.security.Signature;
import java.security.NoSuchAlgorithmException;

    //data = base64urlEncode( header ) + “.” + base64urlEncode( payload
public class JWTSigner {

    private static final Logger logger = LoggerFactory.getLogger(JWTSigner.class);

    public String doit(String header, String payload) 
        throws Exception {

        String encodedHeader  = encode(header);
        String encodedPayload = encode(payload);

        System.out.println("encodedHeader " + encodedHeader);
        System.out.println("encodedPayload " + encodedPayload);

        String toBeSigned = encodedHeader + "." + encodedPayload;
        String signature = signIt(toBeSigned);
        String jwt = toBeSigned + "." + signature;
        return jwt;
    }

    private String signIt(String aToBeSigned) 
        throws Exception {
        try {
            RSASigner rsa = new RSASigner();
            KeyPair pair = rsa.getKeyPair();

            Signature signer = Signature.getInstance("SHA256withRSA");

            signer.initSign(pair.getPrivate());
            signer.update(aToBeSigned.getBytes());
            byte[] signedBytes = signer.sign();

            Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(signedBytes);

            String signature = new String(encodedBytes);
            return signature;
        } catch (NoSuchAlgorithmException e) {
            logger.warn("No such signature", e);
            throw e;
        }
        catch (Exception e) {
            logger.warn("Error ", e);
            throw e;
        }
    }

    private String encode(String aInput) {
        Encoder encoder = Base64.getEncoder();

        byte[] encodedBytes = encoder.encode(aInput.getBytes());

        String output = new String(encodedBytes);
        return output;
    }

}
