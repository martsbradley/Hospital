package martinbradley.hospital.rest;

import org.junit.jupiter.api.Test;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import martinbradley.hospital.web.beans.PatientBean;
import martinbradley.hospital.web.beans.IdentifierBean;

public class RestClientTest
{
    private static final Logger logger = LoggerFactory.getLogger(RestClientTest.class);

    @Test
    public void patient_load_not_found_404()
    {
        Response response = call_load_patient(1991999);

        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
    }

    @Test
    public void successful_save_call()
    {
       String forename = "Frank";
       String surname = getRandomString();

       String xml = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<patient><forename>%s</forename><surname>%s</surname>" +
                "<male>true</male><dob>1976-03-09</dob></patient>", forename, surname);

        PatientBean patientBean =  new PatientBean();
        patientBean.setForename(forename);
        patientBean.setSurname(surname);

        Client client = ClientBuilder.newClient();

        String url = "http://localhost:8080/firstcup/rest/hospital/patient/";
        
        //Entity entity = Entity.entity(xml, MediaType.APPLICATION_XML);
        Entity entity = Entity.entity(patientBean, MediaType.APPLICATION_XML);

        Response response = client.target(url)
                                  .request()
                                  .post(entity, Response.class);


        assertThat(response.getStatusInfo(), is(Response.Status.ACCEPTED));
        logger.info("post response " + response);

        IdentifierBean idBean = response.readEntity(IdentifierBean.class);

        logger.info("Save returned id " + idBean.getId());
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

    //private Response call_post

    private Response call_load_patient(int id)
    {
        Client client = ClientBuilder.newClient();

        String url = String.format("http://localhost:8080/firstcup/rest/hospital/patient/%d",id);
        
        Response response = client.target(url)
                                  .request()
                                  .get(Response.class);
        return response;
    }
}
