package martinbradley.hospital.rest;

import martinbradley.hospital.web.beans.PatientBean;

public class HospitalResourceImpl implements HospitalResource
{
    public PatientBean getPatient(long id)
    {
        PatientBean p =  new PatientBean();
        p.setForename("Martin");
        p.setSurname("Bradley");

        return p;
    }

    public long createPatient(PatientBean patientBean)
    {
        return 0L;
    }
}
