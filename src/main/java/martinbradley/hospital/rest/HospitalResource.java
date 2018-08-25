package martinbradley.hospital.rest;

import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import martinbradley.hospital.web.beans.PatientBean;

@Path("/hospital")
public interface HospitalResource
{
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public PatientBean getPatient(@PathParam("id") long id);

    @POST
    public long createPatient(PatientBean patientBean);
}
