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
import javax.ws.rs.core.GenericType;
import java.util.List;

public class RestClientTestIT
{
    private static final Logger logger = LoggerFactory.getLogger(RestClientTestIT.class);

    @Test
    public void patient_load_not_found_404()
    {
        Response response = call_load_patient(1991999);

        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
    }

    @Test
    public void load_patients_paged()
    {
        Response resp = call_load_patient_paged(1, 8);

        assertThat(resp.getStatusInfo(), is(Response.Status.ACCEPTED));

        List<PatientBean> patients = resp.readEntity(new GenericType<List<PatientBean>>() { });
        //
        //assertThat(patients, is(notN

        System.out.println("patients.size() " +patients.size());
        for (PatientBean pat : patients)
        {
            System.out.println(pat);
        }
    }

    @Test
    public void successful_save_call()
    {
        PatientBean patientBean = new PatientBean();
        patientBean.setForename("Frank");
        patientBean.setSurname(getRandomString());

        Client client = ClientBuilder.newClient();

        String url = "http://localhost:8080/firstcup/rest/hospital/patient/";
        
        Entity entity = Entity.entity(patientBean, MediaType.APPLICATION_JSON);

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
        String url = String.format("http://localhost:8080/firstcup/rest/hospital/patient/%d",id);
        
        Response response = callGetRequest(url);

        return response;
    }

    private Response call_load_patient_paged(int aStart, int aMax)
    {
        String url = String.format("http://localhost:8080/firstcup/rest/hospital/patients?" + 
                                   "start=%d&" +
                                   "max=%d&sortby=forename", 
                                   aStart,
                                   aMax);

        Response response = callGetRequest(url);

        return response;
    }

    private Response callGetRequest(String url)
    {
        Client client = ClientBuilder.newClient();
        Response response = client.target(url)
                                  .request()
                                  .get(Response.class);
        return response;
    }
}
