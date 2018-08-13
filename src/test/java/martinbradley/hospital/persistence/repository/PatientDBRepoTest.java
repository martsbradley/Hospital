package martinbradley.hospital.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.SavePatientResponse;
import mockit.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.not;
import javax.persistence.EntityTransaction;

public class PatientDBRepoTest
{
    @Injectable EntityManager entityManager;
    @Injectable UserTransaction tx;
    @Tested PatientDBRepo impl;

    @Test
    @SuppressWarnings("unchecked")
    public void testOne(@Mocked Patient.SortOrder order)
    {
        new Expectations(){{
            order.getOrder((CriteriaBuilder)any, (Root<Patient>)any);
        }};

        List<Patient> result = impl.pagePatients(0, 1, order);
    }

    @Test
    public void saveNotReturnNull(@Mocked Patient patient)
    {
        SavePatientResponse response = impl.savePatient(patient);
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void can_resave_patient_over_again(@Mocked TypedQuery playerQuery)
    {
        Patient patient = new Patient();
        patient.setId(1L);

        Patient existingPatient = new Patient();
        existingPatient.setId(1L);

        namedqueryReturns(playerQuery, existingPatient);

        SavePatientResponse response = impl.savePatient(patient);

        boolean thereAreErrors = response.getMessages().iterator().hasNext();
        assertThat(thereAreErrors, is(not(true)));
    }

    private void namedqueryReturns(final TypedQuery playerQuery, final Patient ... patients)
    {
	final ArrayList<Patient> patientsWithSameName = new ArrayList<>();
        for (Patient p : patients)
        {
            patientsWithSameName.add(p);
        }

	new Expectations(){{   
	    entityManager.createNamedQuery(anyString, Patient.class);
	    result = playerQuery;

	    playerQuery.getResultList();
	    result = patientsWithSameName;
        }};
    }

    @Test
    public void cannot_save_duplicate_patient(@Mocked TypedQuery playerQuery)
    {
        Patient patient = new Patient();
        patient.setId(1L);

        Patient existingPatient = new Patient();
        existingPatient.setId(4L);

        namedqueryReturns(playerQuery, existingPatient);

        SavePatientResponse response = impl.savePatient(patient);

        boolean thereAreErrors = response.getMessages().iterator().hasNext();
        assertThat(thereAreErrors, is(true));
    }
}