package martinbradley.hospital.web.beans;

import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.web.beans.PatientBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses=SexToBoolean.class)
public interface PatientBeanMapper
{
    @Mapping(target="male",  source="sex",
             qualifiedBy = { SexTranslator.class})
    PatientBean dtoToBean(PatientDTO patient);

    @Mapping(target="sex", source="male",
             qualifiedBy = { SexTranslator.class})
    PatientDTO beanToDTO(PatientBean bean);
}
