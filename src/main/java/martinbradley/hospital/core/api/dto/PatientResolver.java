package martinbradley.hospital.core.api.dto;

import javax.inject.Inject;
import martinbradley.hospital.persistence.repository.PatientDBRepo;
import martinbradley.hospital.core.domain.Patient;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Model;

//@ApplicationScoped
public class PatientResolver {
  //@Inject PatientDBRepo repo;
  //private static final Logger logger = LoggerFactory.getLogger(PatientResolver.class);

  //public Patient  resolve(UploadedImageDTO dto, @TargetType Class<Patient> type) {
  //    logger.info("Resolve called");
  //    T patient =  repo.loadById(dto.getPatientId());
  //    return patient;
  //}

  //public long toReference(Patient aPatient) {
  //    return aPatient.getId();
  //}
}
