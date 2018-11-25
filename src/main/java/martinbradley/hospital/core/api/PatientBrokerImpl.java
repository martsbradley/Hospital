package martinbradley.hospital.core.api;

import java.util.Collections;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.domain.*;
import martinbradley.hospital.persistence.repository.PatientDBRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import martinbradley.hospital.core.api.dto.*;
import org.mapstruct.factory.Mappers;
import martinbradley.hospital.core.domain.SavePatientResponse;
import martinbradley.hospital.web.beans.PageInfo;

@Model
public class PatientBrokerImpl implements PatientBroker
{
    @Inject PatientDBRepo repo;
    private static final Logger logger = LoggerFactory.getLogger(PatientBrokerImpl.class);

    @Override
    public List<PatientDTO> getPatientsPaged(PageInfo aPageInfo)
    {
        Patient.SortOrder ordering = Patient.SortOrder.find(aPageInfo.getSortField(), 
                                                            aPageInfo.isAscending());
  
       
        List<Patient> patients = repo.pagePatients(aPageInfo.getStartingAt(), 
                                                   aPageInfo.getMaxPerPage(), 
                                                   ordering);

        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);

        List<PatientDTO> dtoResults = new ArrayList<>();

        for (Patient p: patients)
        {
          //logger.info(String.format("patient %s has %d prescriptions",
          //                          p.getForename(), 
          //                          p.getPrescription().size()));
            PatientDTO pat = mapper.patientToDTO(p);
            dtoResults.add(pat);
        }

        return dtoResults;
    }

    public int getPatientCount()
    {
        return repo.getTotalPatients();
    }

    @Override
    public long savePatient(PatientDTO aPatientDTO, MessageCollection aMessages)
    {
        logger.debug("savePatient" + aPatientDTO);

        boolean errors = validatePatient(aPatientDTO, aMessages);

        if (errors)
        {
	    logger.warn("savePatient failed.");
            return -1;
        }

        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);

        Patient pat = mapper.dtoToPatient(aPatientDTO);

        List<Prescription> pres = pat.getPrescription();
        for (Prescription p : pres)
        {
            p.setPatient(pat);
        }

        SavePatientResponse repoResponse = repo.savePatient(pat);
        if (repoResponse.hasMessages())
        {
            logger.info("Repo hit problems when saving");
            aMessages.addAll(repoResponse.getMessages());
            return -1;
        }
        return repoResponse.getPatient().getId();
    }

    private boolean validatePatient(PatientDTO aPatientDTO, MessageCollection aMessages)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(aPatientDTO);

        if (!violations.isEmpty())
        {
            ConstraintToMessageConverter conv = new ConstraintToMessageConverter();

            for (ConstraintViolation<PatientDTO> c : violations)
            {
                Message message = conv.getMessage(c);
                if (message == null)
                {
                    logger.warn("Cannot find message for " + c);
                }
                else
                {
                    aMessages.add(message);
                }
            }
        }
        return !violations.isEmpty();
    }

    public PatientDTO deletePatient(PatientDTO aPatientDTO)
    {
        logger.debug("deletePatient" + aPatientDTO);
        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);

        Patient pat = mapper.dtoToPatient(aPatientDTO);
        repo.deletePatient(pat);
        //TODO result should come from resp
        return aPatientDTO;
    }

    @Override
    public PatientDTO loadById(long id)
    {
        Patient loadedPatient = repo.loadById(id);

        if (loadedPatient == null)
        {
            logger.info("returning null for patient " + id);
            return null;
        }

        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);
        PatientDTO dto = mapper.patientToDTO(loadedPatient);
        logger.debug(String.format("Loaded(%d) and got %s",id,dto));
        for (PrescriptionDTO p : dto.getPrescription())
        {
            logger.debug("---> A prescription" + p);
        }
        return dto;
    }
}
