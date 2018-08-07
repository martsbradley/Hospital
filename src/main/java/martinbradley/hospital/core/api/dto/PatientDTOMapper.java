package martinbradley.hospital.core.api.dto;

import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses=SexMapper.class)
public interface PatientDTOMapper
{
    PatientDTO patientToDTO(Patient patient);
    Patient    dtoToPatient(PatientDTO bean);

    default String medicinetoString(Medicine value)
    {
        return value.getName();
    }
}
