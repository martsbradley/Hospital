package martinbradley.hospital.core.api.dto;

import org.junit.jupiter.api.Test;
import mockit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.Prescription;
import org.mapstruct.factory.Mappers;
import martinbradley.hospital.core.domain.Sex;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class PatientDTOMapperTest
{
    private static final Logger logger = LoggerFactory.getLogger(PatientDTOMapperTest.class);
    @Test
    public void dto_maps_to_patient()
    {
        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);
        PatientDTO dto = new PatientDTO();

	LocalDate myDate = LocalDate.now();
        dto.setForename("martin");
        dto.setSurname("bradley");
        dto.setSex("m");
	dto.setDob(myDate);

        Patient pat = mapper.dtoToPatient(dto);

        assertEquals("martin",  pat.getForename());
        assertEquals("bradley", pat.getSurname());
        assertEquals(Sex.Male,  pat.getSex());
        assertEquals(myDate,    pat.getDob());
    }

    @Test
    public void patient_maps_to_dto()
    {
        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);
        Patient pat = new Patient();

	LocalDate myDate = LocalDate.now();
        pat.setId(1L);
        pat.setForename("martin");
        pat.setSurname("bradley");
        pat.setSex(Sex.Female);
	pat.setDob(myDate);

        PatientDTO dto = mapper.patientToDTO(pat);

        assertEquals("martin",  dto.getForename());
        assertEquals("bradley", dto.getSurname());
        assertEquals("F",  dto.getSex());
        assertEquals(myDate,    dto.getDob());
    }
    @Test
    public void patient_with_prescripions_maps_to_dto()
    {
        logger.warn("running patient_with_prescripions_maps_to_dto");
        final PatientDTOMapper mapper = Mappers.getMapper(PatientDTOMapper.class);

        final Patient patient = new Patient();
        patient.setForename("Martin");
        patient.setSurname("Bradley");
        patient.setDob(LocalDate.now());
        patient.setSex(Sex.Male);
        patient.setPrescription(new ArrayList<Prescription>());

        PatientDTO dto = mapper.patientToDTO(patient);

        assertThat(dto, is(notNullValue()));

        List<PrescriptionDTO> prescription = dto.getPrescription();
        
        assertEquals(0, prescription.size());

      //assertEquals("martin",  dto.getForename());
      //assertEquals("bradley", dto.getSurname());
      //assertEquals("F",  dto.getSex());
      //assertEquals(myDate,    dto.getDob());
    }
}
