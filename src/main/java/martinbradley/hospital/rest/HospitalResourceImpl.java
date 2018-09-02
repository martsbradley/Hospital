package martinbradley.hospital.rest;

import martinbradley.hospital.web.beans.PatientBean;
import martinbradley.hospital.web.beans.IdentifierBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.web.api.PatientHandler;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status;
import martinbradley.hospital.web.beans.PageInfo;
import static martinbradley.hospital.web.beans.PageInfo.PageInfoBuilder;
import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.core.GenericEntity;

import martinbradley.hospital.core.api.dto.MessageCollection;

@Path("/hospital")
public class HospitalResourceImpl 
{
    @Inject PatientHandler patientHandler;
    private static Logger logger = LoggerFactory.getLogger(HospitalResourceImpl.class);

    @GET
    @Path("patient/{id}")
    @Produces("application/xml")
    public Response getPatient(@PathParam("id") long patientId)
    {
        logger.info("loading " + patientId);
        PatientBean patient = patientHandler.loadById(patientId);

        if (patient == null)
        {
            return Response.status(Status.NOT_FOUND)
                           .type(MediaType.APPLICATION_XML)
                           .build();
        }

        return Response.accepted(patient)
                       .type(MediaType.APPLICATION_XML)
                       .build();
    }

    //"/patient?start=1&max=5?sortby=forname"
    @GET
    @Path("patients/")
    @Produces("application/xml")
    public Response createPatient(@QueryParam("start")  int aStart,
                                  @QueryParam("max")    int aMax,
                                  @QueryParam("sortby") String aSortBy)
    {
        PageInfo pageInfo  = new PageInfoBuilder()
                                 .setStartAt(aStart)
                                 .setMaxPerPage(aMax)
                                 .setSortField(aSortBy)
                                 .build();
        List<PatientBean> patients = patientHandler.pagePatients(pageInfo);

        GenericEntity<List<PatientBean>> entity = new GenericEntity<List<PatientBean>>(patients) {};

        return Response.accepted(entity)
                       .type(MediaType.APPLICATION_XML)
                       .build();
    }

    @POST
    @Path("patient")
    @Produces("application/xml")
    public Response createPatient(PatientBean patientBean)
    {
        logger.warn("createPatient passed " + patientBean);

        MessageCollection messages = new MessageCollection();
        Long id = patientHandler.savePatient(patientBean, messages);
        IdentifierBean ident = new IdentifierBean();
        ident.setId(id);
        
        if (id == null || messages.hasMessages())
        {
            return Response.status(Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_XML)
                           .entity("Could not get it together")
                           .build();
        }

        return Response.accepted(ident)
                       .type(MediaType.APPLICATION_XML)
                       .build();
    }
}
