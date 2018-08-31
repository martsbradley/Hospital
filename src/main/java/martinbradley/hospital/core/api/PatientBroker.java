package martinbradley.hospital.core.api;

import java.util.List;
import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.api.dto.MessageCollection;

public interface PatientBroker
{
    public List<PatientDTO> getPatientsPaged(int start,
                                          int howMany,
                                          String orderColumn,
                                          boolean isAscending);
    public int getPatientCount();

    public long savePatient(PatientDTO aPatient, MessageCollection aMessages);

    public PatientDTO deletePatient(PatientDTO aPatient);

    public PatientDTO loadById(long id);
}
