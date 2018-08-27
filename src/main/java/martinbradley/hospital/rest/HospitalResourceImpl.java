package martinbradley.hospital.rest;

import martinbradley.hospital.web.beans.PatientBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.web.api.PatientHandler;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

@Path("/hospital")
public class HospitalResourceImpl 
{
    @Inject PatientHandler patientHandler;
    private static Logger logger = LoggerFactory.getLogger(HospitalResourceImpl.class);

    @GET
    @Path("{id}")
    @Produces("application/xml")
    public PatientBean getPatient(@PathParam("id") long patientId)
    {
        logger.info("loading " + patientId);
        logger.info("patientHandler is null " + (patientHandler == null));
        PatientBean patient = patientHandler.loadById(patientId);
      //PatientBean p =  new PatientBean();
      //p.setForename("Martin");
      //p.setSurname("Bradley");

        return patient;
    }

    @POST
    public long createPatient(PatientBean patientBean)
    {
        return 0L;
    }
}
