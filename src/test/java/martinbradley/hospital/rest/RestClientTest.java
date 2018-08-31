package martinbradley.hospital.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class RestClientTest
{
    private static final Logger logger = LoggerFactory.getLogger(RestClientTest.class);

    @BeforeEach
    public void beforeEachTest()
    {
    }

    @Test
    public void callGet()
    {
        int id = 1;
        Client client = ClientBuilder.newClient();

        String url = String.format("http://localhost:8080/firstcup/rest/hospital/patient/%d",id);
        
        String response = client.target(url)
                                .request()
                                .get(String.class);
        assertNotNull(response);
    }

    @Test
    public void callPost()
    {
       String forename = "Frank";
       String surname = getRandomString();

       String xml = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<patient><forename>%s</forename><surname>%s</surname>" +
                "<male>true</male><dob>1976-03-09</dob></patient>", forename, surname);

        Client client = ClientBuilder.newClient();

        String url = "http://localhost:8080/firstcup/rest/hospital/patient/";
        
        Long id = client.target(url)
                                .request()
                                .post(Entity.entity(xml, MediaType.APPLICATION_XML), Long.class);
        logger.info("post response " + id);
    }

    private String getRandomString()
    {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);

        for (int i = 0; i < targetStringLength; i++) 
        {
            int randomLimitedInt = leftLimit + (int) 
              (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
}
