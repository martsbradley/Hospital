package martinbradley.hospital.core.api.dto;

import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.Medicine;
import martinbradley.hospital.core.domain.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

@Mapper(uses=SexMapper.class)
public interface PatientDTOMapper
{
    PatientDTO patientToDTO(Patient patient);
    Patient    dtoToPatient(PatientDTO bean);

    /** There is a circular reference
     *  from patient to prescription but also 
     *  from prescription.patient back to the same patient
     *
     *  To handle this the patient field is ignored when mapping the
     *  prescription, but then the AfterMapping of the Patient/PatientDTO
     *  updates the references.
     */
    @Mapping( target = "patient", ignore = true )
    PrescriptionDTO prescriptionToDTO(Prescription prescription);

    @Mapping( target = "patient", ignore = true )
    Prescription prescriptionDTOToPrescription(PrescriptionDTO prescription);

    @AfterMapping
    default void addPatientDtoBackReference(@MappingTarget PatientDTO target) 
    {
        for (PrescriptionDTO child : target.getPrescription() ) 
        {
            child.setPatient(target);
        }
    }

    @AfterMapping
    default void addPatientBackReference(@MappingTarget Patient target) 
    {
        for (Prescription child : target.getPrescription() ) 
        {
            child.setPatient(target);
        }
    }
}
