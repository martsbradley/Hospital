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

@Model
public class PatientBrokerImpl implements PatientBroker
{
    @Inject PatientDBRepo repo;
    private static final Logger logger = LoggerFactory.getLogger(PatientBrokerImpl.class);

    @Override
    public List<PatientDTO> getPatientsPaged(int start,
                                             int howMany,
                                             String orderColumn,
                                             boolean isAscending)
    {
        Patient.SortOrder ordering = Patient.SortOrder.find(orderColumn, isAscending);
  
       
        List<Patient> patients = repo.pagePatients(start, howMany, ordering);

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
    public PatientDTO savePatient(PatientDTO aPatientDTO)
    {
        logger.info("PatientBrokerImpl.savePatient" + aPatientDTO);

        MessageCollection messages = validatePatient(aPatientDTO);

        if (messages.hasMessages())
        {
            aPatientDTO.setMessages(messages);
            return aPatientDTO;
        }

        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);

        Patient pat = mapper.dtoToPatient(aPatientDTO);
        logger.info("Converted to the pat " + pat);


        List<Prescription> pres = pat.getPrescription();
        for (Prescription p : pres)
        {
            p.setPatient(pat);
        }

        SavePatientResponse response = repo.savePatient(pat);

        PatientDTO savedPatientDTO = mapper.patientToDTO(response.getPatient());
        savedPatientDTO.setMessages(response.getMessages());

        return savedPatientDTO;
    }

    private MessageCollection validatePatient(PatientDTO aPatientDTO)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(aPatientDTO);

        MessageCollection messages = new MessageCollection();
        
        if (!violations.isEmpty())
        {
            ConstraintToMessageConverter conv = new ConstraintToMessageConverter();

            for (ConstraintViolation<PatientDTO> c : violations)
            {
                Message message = conv.getMessage(c);
                messages.add(message);
            }
        }
        return messages;
    }

    public PatientDTO deletePatient(PatientDTO aPatientDTO)
    {
        logger.info("PatientBrokerImpl.deletePatient" + aPatientDTO);
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
        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);
        PatientDTO dto = mapper.patientToDTO(loadedPatient);
        logger.info(String.format("Loaded(%d) and got %s",id,dto));
        for (PrescriptionDTO p : dto.getPrescription())
        {
            logger.info("---> A prescription" + p);
        }
        return dto;
    }
}
