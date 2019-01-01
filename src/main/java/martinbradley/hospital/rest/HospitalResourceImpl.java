package martinbradley.hospital.rest;

import martinbradley.hospital.web.beans.PatientBean;
import martinbradley.hospital.web.beans.MedicineBean;
import martinbradley.hospital.web.beans.ValidationErrors;
import martinbradley.hospital.web.beans.ValidationError;

import martinbradley.hospital.web.beans.IdentifierBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.web.api.PatientHandler;
import martinbradley.hospital.web.api.MedicineHandler;
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
import javax.ws.rs.NotFoundException;
import static javax.ws.rs.core.Response.Status;
import martinbradley.hospital.web.beans.PageInfo;
import static martinbradley.hospital.web.beans.PageInfo.PageInfoBuilder;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import javax.ws.rs.core.GenericEntity;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.security.RolesAllowed;

import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.auth0.SecuredRestfulMethod;

@Path("/hospital")
public class HospitalResourceImpl 
{
    private static final String ADMIN_ROLE="AdminUsers";
    @Inject PatientHandler patientHandler;
    @Inject MedicineHandler medicineHandler;
    private static Logger logger = LoggerFactory.getLogger(HospitalResourceImpl.class);


    @GET
    @Path("patient/{id}")
    @Produces("application/json")
    public Response getPatient(@PathParam("id") long patientId)
    {
        logger.info("getPatient byId " + patientId);
        PatientBean patient = patientHandler.loadById(patientId);

        if (patient == null)
        {
          //return Response.status(Status.NOT_FOUND)
          //               .type(MediaType.APPLICATION_JSON)
          //               .build();
            throw new NotFoundException();
        }

        return Response.accepted(patient)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    //"/patient?start=1&max=5?sortby=forname"
    @GET
    @Path("patients/")
    @Produces("application/json")
    @SecuredRestfulMethod(scopes={"read:patients"})
    public Response pagePatients(@QueryParam("start")  int aStart,
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
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    //"/medicines?start=1&max=5&sortby=forname&filter=me
    @GET
    @Path("medicines/")
    @Produces("application/json")
    public Response pageMedicines(@QueryParam("start")  int aStart,
                                  @QueryParam("max")    int aMax,
                                  @QueryParam("sortby") String aSortBy,
                                  @QueryParam("filter") String aFilter) {
        PageInfo pageInfo  = new PageInfoBuilder()
                                 .setStartAt(aStart)
                                 .setMaxPerPage(aMax)
                                 .setSortField(aSortBy)
                                 .setFilter(aFilter)
                                 .build();

        logger.info("pageMedicines " + pageInfo);

        List<MedicineBean> medicines = medicineHandler.pageMedicines(pageInfo);

        GenericEntity<List<MedicineBean>> entity = new GenericEntity<List<MedicineBean>>(medicines) {};

        return Response.accepted(entity)
                       .type(MediaType.APPLICATION_JSON)
                       .build();

    }



    @POST
    @Path("patient")
    @Produces("application/json")
    public Response savePatient(PatientBean patientBean)
    {
        logger.info("savePatient " + patientBean);
	ValidationErrors validationResult = validate(patientBean);

	if (validationResult.hasErrors()) {
	    logger.info("savePatient returning validation message");
            return Response.status(Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_JSON)
                           //.entity("bla")
                           .entity(validationResult)
                           .build();
	}


        MessageCollection messages = new MessageCollection();
        Long id = patientHandler.savePatient(patientBean, messages);
        IdentifierBean ident = new IdentifierBean();
        ident.setId(id);
        
        if (id == null || messages.hasMessages())
        {
            logger.info("Failed to save" + messages);
            return Response.status(Status.BAD_REQUEST)
                           .type(MediaType.APPLICATION_JSON)
                           .entity(messages.toString())
                           .build();
        }

        logger.info("savePatient successful");

        return Response.accepted(ident)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    private ValidationErrors validate(PatientBean patientBean) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PatientBean>> violations = validator.validate(patientBean);
        logger.debug("violations has " + violations.size());

	ValidationErrors errors = new ValidationErrors();

	for (ConstraintViolation<PatientBean> violation: violations)
	{
	    String invalidValue = (String) violation.getInvalidValue();
	    String message = violation.getMessage();
	    String path = violation.getPropertyPath().toString();

	    logger.warn("Found constraint violation. Value: '" + invalidValue + "' Message: " + message);
	    logger.warn("path." + path);

	    ValidationError error = new ValidationError();
	    error.setField(path);
	    error.setMessage(message);
	    errors.add(error);
	}
	return errors;
    }


    @GET
    @Path("patients/total")
    @Produces("application/json")
    @SecuredRestfulMethod(scopes={"read:patients"})
    public Response totalPatients()
    {
        logger.debug("totalPatients called");
        int total = patientHandler.getTotalPatients();

        return Response.accepted(total)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    @GET
    @Path("medicines/total")
    @Produces("application/json")
    public Response totalMedicines(@QueryParam("filter") String filter)
    {
        logger.debug("totalMedicines called");
        int total = medicineHandler.getTotalMedicines(filter);

        return Response.accepted(total)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }


    @GET
    @Path("userDetail")
    @Produces("application/text")
    public Response userDetails(@Context SecurityContext sc) {

        String username = "";

        if (isAdminLoggedIn(sc)) {
            username = sc.getUserPrincipal().getName();
        }
        logger.info("userDetails :'"+ username + "'");

        return Response.accepted(username)
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }

    @GET
    @Path("isAdmin")
    @Produces("application/text")
    public Response isAdmin(@Context SecurityContext sc) {

        boolean isAdmin = isAdminLoggedIn(sc);

        logger.debug("isAdmin :"+ isAdmin);
        return Response.accepted(isAdmin)
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }


    @POST
    @Path("logOff")
    @RolesAllowed({ ADMIN_ROLE})
    @Produces("application/text")
    public Response isAdmin(@Context HttpServletRequest req) {

        req.getSession().invalidate();

        return Response.status(Status.UNAUTHORIZED)
                       .type(MediaType.TEXT_PLAIN)
                       .build();
    }

    private boolean isAdminLoggedIn(SecurityContext sc) {
        boolean isAdmin = sc.isUserInRole(ADMIN_ROLE);
        return isAdmin;
    }
}
