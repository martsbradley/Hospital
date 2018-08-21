package martinbradley.hospital.web.beans;

import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.api.dto.PrescriptionDTO;
import martinbradley.hospital.web.beans.PatientBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

@Mapper(uses=SexToBoolean.class)
public interface PatientBeanMapper
{
    @Mapping(target="male",  source="sex",
             qualifiedBy = { SexTranslator.class})
    PatientBean dtoToBean(PatientDTO patient);

    @Mapping(target="sex", source="male",
             qualifiedBy = { SexTranslator.class})
    PatientDTO beanToDTO(PatientBean bean);

    /** There is a circular reference
     *  from patient to prescription but also 
     *  from prescription.patient back to the same patient
     *
     *  To handle this the patient field is ignored when mapping the
     *  prescription, but then the AfterMapping of the Patient/PatientDTO
     *  updates the references.
     */

    @Mapping( target = "patient", ignore = true )
    PrescriptionDTO prescriptionBeanToDTO(PrescriptionBean bean);

    @Mapping( target = "patient", ignore = true )
    PrescriptionBean prescriptionDTOToBean(PrescriptionDTO prescription);

    @AfterMapping
    default void addPatientBeanBackReference(@MappingTarget PatientBean target) 
    {
        for (PrescriptionBean child : target.getPrescription() ) 
        {
            child.setPatient(target);
        }
    }

    @AfterMapping
    default void addPatientBackReference(@MappingTarget PatientDTO target) 
    {
        for (PrescriptionDTO child : target.getPrescription() ) 
        {
            child.setPatient(target);
        }
    }
}
