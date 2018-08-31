package martinbradley.hospital.core.api;

import java.util.List;
import martinbradley.hospital.core.api.dto.PatientDTO;
import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.hospital.web.beans.PageInfo;

public interface PatientBroker
{
    public List<PatientDTO> getPatientsPaged(PageInfo aPageInfo);

    public int getPatientCount();

    public long savePatient(PatientDTO aPatient, MessageCollection aMessages);

    public PatientDTO deletePatient(PatientDTO aPatient);

    public PatientDTO loadById(long id);
}
