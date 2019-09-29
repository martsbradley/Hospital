
package martinbradley.hospital.web.beans;

import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.api.dto.PrescriptionDTO;
import martinbradley.hospital.web.beans.PatientBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import martinbradley.hospital.rest.ImageUploaded;
import martinbradley.hospital.core.api.dto.UploadedImageDTO;

@Mapper
public interface UploadBeanMapper
{
    ImageUploaded dtoToBean(UploadedImageDTO dto);

    UploadedImageDTO beanToDTO(ImageUploaded bean);
}
