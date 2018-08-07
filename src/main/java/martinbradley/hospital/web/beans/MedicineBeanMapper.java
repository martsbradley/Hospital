package martinbradley.hospital.web.beans;

import martinbradley.hospital.core.api.dto.MedicineDTO;
import martinbradley.hospital.web.beans.MedicineBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MedicineBeanMapper
{
    MedicineBean dtoToBean(MedicineDTO medicine);

    MedicineDTO beanToDTO(MedicineBean bean);
}
