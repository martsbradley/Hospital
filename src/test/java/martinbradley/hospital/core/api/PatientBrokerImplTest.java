package martinbradley.hospital.core.api;
import martinbradley.hospital.core.api.dto.MessageCollection;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.Prescription;
import martinbradley.hospital.core.domain.Sex;
import mockit.*;
import martinbradley.hospital.persistence.repository.PatientDBRepo;
import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.api.dto.PrescriptionDTO;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.MatcherAssert.assertThat;
import javax.validation.ConstraintViolation;
import java.util.Set;
import java.time.LocalDate;
import java.util.HashSet;

public class PatientBrokerImplTest
{
    @Mocked PatientDBRepo mockRepo;
    private PatientBrokerImpl impl;

    @BeforeEach
    public void setMeUp()
    {
        impl = new PatientBrokerImpl();
        impl.repo = mockRepo;
    }


    @Test
    public void getPatients_CallsRepo()
    {
        Patient p1 = new Patient();
        Patient p2 = new Patient();
        final List<Patient> myResults = Arrays.asList(p1,p2);
    
        new Expectations(){{
            mockRepo.pagePatients(1, 10, (Patient.SortOrder)any); result = myResults;
        }};

        List<PatientDTO> list = impl.getPatientsPaged(1, 10, "forename", true);
        assertThat(list, is(not(empty())));
    }

    @Test
    public void savePatient_CallsRepo()
    {
        PatientDTO patDTO = new PatientDTO();
        patDTO.setForename("Martin");
        patDTO.setSurname("Bradley");
        patDTO.setDob(LocalDate.now());
        patDTO.setSex("M");

        new Expectations(){{
            mockRepo.savePatient((Patient)any);
        }};

        Object result = impl.savePatient(patDTO);
        assertNotNull(result);
    }
    @Test
    public void deletePatient_CallsRepo()
    {
        PatientDTO patDTO = new PatientDTO();
        patDTO.setForename("Martin");
        patDTO.setSurname("Bradley");
        patDTO.setSex("F");

        new Expectations(){{
            mockRepo.deletePatient((Patient)any);
        }};

        Object result = impl.deletePatient(patDTO);
        assertNotNull(result);
    }
    @Test
    public void savePatient_Validates()
    {
        PatientDTO patDTO = new PatientDTO();
        patDTO.setForename("Martin");
        patDTO.setSurname(""); // surname too short.
        patDTO.setDob(LocalDate.now());
        patDTO.setSex("M");

        PatientDTO result = impl.savePatient(patDTO);
        assertNotNull(result);

        MessageCollection errors = result.getMessages();
        assertNotNull(errors);

        //   TODO
        //fail("The validation needs coded and this test needs fixed.....");
        //Set<ConstraintViolation<PatientDTO>> violations = result.getViolations();
        //assertThat(violations, is(not(empty())));
    }

    @Test
    public void loadById_calls_resp()
    {
        final Patient patient = new Patient();
        patient.setForename("Martin");
        patient.setSurname("Bradley");
        patient.setDob(LocalDate.now());
        patient.setSex(Sex.Male);
        patient.setPrescription(new HashSet<Prescription>());

        new Expectations(){{
            mockRepo.loadById(anyLong); result= patient;
        }};

        impl.loadById(1L);

        new Verifications(){{
            mockRepo.loadById(anyLong); times = 1;
        }};
    }
}