package martinbradley.hospital.web.api;
import javax.inject.Named;
import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import martinbradley.hospital.persistence.repository.PatientDBRepo;
import martinbradley.hospital.web.beans.PatientBean;
import javax.enterprise.inject.Model;
import martinbradley.hospital.web.beans.PatientBeanMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import martinbradley.hospital.core.api.PatientBroker;
import martinbradley.hospital.core.api.dto.*;
import javax.validation.ConstraintViolation;
import java.util.Set;
import martinbradley.hospital.web.beans.PageInfo;

@ApplicationScoped
@Named
public class PatientHandler
{
    private static final Logger logger = LoggerFactory.getLogger(PatientHandler.class);
    final PatientBeanMapper mapper = Mappers.getMapper(PatientBeanMapper.class);

    @Inject PatientBroker patientBroker;

    public List<PatientBean> pagePatients(PageInfo aPageInfo)
    {
        List<PatientDTO> patients = patientBroker.getPatientsPaged(aPageInfo);

        ArrayList<PatientBean> beans = new ArrayList<>();
        for (PatientDTO p: patients)
        {
            PatientBean bean = mapper.dtoToBean(p);
            beans.add(bean);
        }
        return beans;
    }
    public int getTotalPatients()
    {
        return patientBroker.getPatientCount();
    }

    public long savePatient(PatientBean patientBean, MessageCollection aMessages)
    {
        logger.info("Save Patient: " + patientBean);
        PatientDTO patient = mapper.beanToDTO(patientBean);

        long patientId = patientBroker.savePatient(patient, aMessages);

        logger.debug("Save Patient: has messages? " + aMessages.hasMessages());

        /*
         * Web page has constraints.   These are shown on the webpage
         *                             Like when a form field is not populated.
         * DTO has constraints.        If a webpage constraint is missing then 
         *                             it is not up to the DTO validation to
         *                             double check.  But the DTO validation 
         *                             will prevent the form submition.
         *                             Would be nice to show an error.
         *
         * Business has constraints.   Can't have two new medicines named
         *                             the same.   Also need to show 
         *                             generic errors.
         *
         * */


        return patientId;
    }

    public void deletePatient(PatientBean patientBean)
    {
        logger.info("Delete Patient: " + patientBean);
        PatientDTO patient = mapper.beanToDTO(patientBean);
        patientBroker.deletePatient(patient);
    }

    public PatientBean loadById(long id)
    {
        PatientDTO patientDTO = patientBroker.loadById(id);
        logger.info(String.format("loadById(%d) returned %s",id, patientDTO));
        PatientBean bean = mapper.dtoToBean(patientDTO);
        return bean;
    }
}
