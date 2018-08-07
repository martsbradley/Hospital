package martinbradley.hospital.web.beans;

import org.junit.jupiter.api.Test;
import mockit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import martinbradley.hospital.core.domain.Patient;
import org.mapstruct.factory.Mappers;
import martinbradley.hospital.core.domain.Sex;
import martinbradley.hospital.core.api.dto.PatientDTO;
import java.time.LocalDate;

public class PatientBeanMapperTest
{
    @Test
    public void dto_maps_to_bean()
    {
        final PatientBeanMapper mapper = Mappers.getMapper(PatientBeanMapper.class);
        PatientDTO dto = new PatientDTO();

	LocalDate myDate = LocalDate.now();
        dto.setForename("martin");
        dto.setSurname("bradley");
        dto.setSex("m");
	dto.setDob(myDate);

        PatientBean patBean = mapper.dtoToBean(dto);

        assertEquals("martin",  patBean.getForename());
        assertEquals("bradley", patBean.getSurname());
        assertTrue(patBean.isMale());
        assertEquals(myDate,    patBean.getDob());
    }

    @Test
    public void bean_Maps_to_DTO()
    {
        final PatientBeanMapper mapper = Mappers.getMapper(PatientBeanMapper.class);
        PatientBean patBean = new PatientBean();

	LocalDate myDate = LocalDate.now();
        patBean.setForename("martin");
        patBean.setSurname("bradley");
        patBean.setMale(false);
	patBean.setDob(myDate);

        PatientDTO dto = mapper.beanToDTO(patBean);

        assertEquals("martin",  dto.getForename());
        assertEquals("bradley", dto.getSurname());
        assertEquals("F", dto.getSex());
        assertEquals(myDate,    dto.getDob());
    }
}
