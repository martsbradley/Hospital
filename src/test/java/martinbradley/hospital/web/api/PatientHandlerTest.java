package martinbradley.hospital.web.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mockit.*;
import martinbradley.hospital.web.beans.PatientBean;
import martinbradley.hospital.core.api.PatientBrokerImpl;
import martinbradley.hospital.core.api.dto.PatientDTO;

public class PatientHandlerTest
{
    @Mocked PatientBrokerImpl patientBroker;
    PatientHandler impl;

    @BeforeEach
    public void setMeUp()
    {
        impl = new PatientHandler();
        impl.patientBroker = patientBroker;
    }

    @Test
    public void savePatient_calls_PatientBroker()
    {
        PatientBean pat = new PatientBean();

        new Expectations(){{
            patientBroker.savePatient((PatientDTO)any);
        }};

        impl.savePatient(pat);
    }
    @Test
    public void deletePatient_calls_PatientBroker()
    {
        PatientBean pat = new PatientBean();

        new Expectations(){{
            patientBroker.deletePatient((PatientDTO)any);
        }};

        impl.deletePatient(pat);
    }

    @Test
    public void loadByIdCallsRepository()
    {
        final PatientDTO patient = new PatientDTO();
        patient.setForename("Martin");
        patient.setSurname("Bradley");
        patient.setSex("Male");
        new Expectations(){{
            patientBroker.loadById(anyLong); result = patient; 
        }};

        impl.loadById(1L);

        new Verifications(){{
            patientBroker.loadById(anyLong); times = 1;
        }};
    }
}
