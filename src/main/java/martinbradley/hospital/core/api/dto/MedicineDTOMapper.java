package martinbradley.hospital.core.api.dto;

import martinbradley.hospital.core.api.dto.MedicineDTO;

import martinbradley.hospital.core.domain.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MedicineDTOMapper
{
    MedicineDTO medicineToDTO(Medicine patient);
    Medicine    dtoToMedicine(MedicineDTO bean);
}
