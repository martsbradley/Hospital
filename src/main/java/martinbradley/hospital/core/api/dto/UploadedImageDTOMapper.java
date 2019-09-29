package martinbradley.hospital.core.api.dto;

import martinbradley.hospital.core.domain.UploadedImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.persistence.repository.PatientDBRepo;
import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import martinbradley.hospital.core.domain.Patient;

//@ApplicationScoped
@Mapper
public interface UploadedImageDTOMapper
{
    static final Logger logger = LoggerFactory.getLogger(UploadedImageDTOMapper.class);

    UploadedImageDTO uploadedImageToDTO(UploadedImage bean);

    UploadedImage    dtoToUploadedImage(UploadedImageDTO dto);


  //@Mappings({
  //            @Mapping(target = "patientId", source = "patient")})
  //@Mappings({
  //      @Mapping(target = "patient", source = "patientId")}) 
  ////@Inject PatientDBRepo repo;

  //Long toPatientId(Patient patient) {
  //    return patient.getId();
  //}

  //Patient toPatientId(Long patientId) {
  //    logger.info("Resolve xxx called");
  //    T patient =  repo.loadById(dto.getPatientId());
  //    return patient;
  //}
}
