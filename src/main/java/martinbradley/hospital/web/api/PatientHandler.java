package martinbradley.hospital.web.api;
import javax.inject.Named;
import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import martinbradley.hospital.web.beans.PatientBean;
import javax.enterprise.inject.Model;
import martinbradley.hospital.web.beans.PatientBeanMapper;
import martinbradley.hospital.web.beans.UploadBeanMapper;
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
import martinbradley.hospital.rest.ImageUploaded;
import java.time.LocalDateTime;

@ApplicationScoped
@Named
public class PatientHandler
{
    private static final Logger logger = LoggerFactory.getLogger(PatientHandler.class);
    final PatientBeanMapper patientMapper = Mappers.getMapper(PatientBeanMapper.class);

    @Inject PatientBroker patientBroker;

    public List<PatientBean> pagePatients(PageInfo aPageInfo)
    {
        List<PatientDTO> patients = patientBroker.getPatientsPaged(aPageInfo);

        ArrayList<PatientBean> beans = new ArrayList<>();
        for (PatientDTO p: patients)
        {
            PatientBean bean = patientMapper.dtoToBean(p);
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
        PatientDTO patient = patientMapper.beanToDTO(patientBean);

        long patientId = patientBroker.savePatient(patient, aMessages);

        logger.info("Save Patient: has messages? " + aMessages.hasMessages());

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
        PatientDTO patient = patientMapper.beanToDTO(patientBean);
        patientBroker.deletePatient(patient);
    }

    public PatientBean loadById(long id)
    {
        PatientDTO patientDTO = patientBroker.loadById(id);
        logger.info(String.format("loadById(%d) returned %s",id, patientDTO));
        PatientBean bean = patientMapper.dtoToBean(patientDTO);
        return bean;
    }

    public void saveImage(long patientId, ImageUploaded upload) {

        logger.info("saveImage");
        final UploadBeanMapper uploadMapper = Mappers.getMapper(UploadBeanMapper.class);
        UploadedImageDTO dto = uploadMapper.beanToDTO(upload);

        dto.setPatientId(patientId);
        dto.setName("Martyname");
        dto.setBucket("Martybuck");
        dto.setDateUploaded(LocalDateTime.now());


        logger.info("saveImage calling broker.");
        patientBroker.saveImage(dto);
        logger.info("saveImage finished.");
    }

}
