package martinbradley.hospital.web.beans;

import org.junit.jupiter.api.Test;
import mockit.*;
import java.util.logging.Logger;
//  import martinbradley.hospital.web.api.PatientHandler;
//  import martinbradley.jsf.util.JSFUtil;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
public class PrescriptionCtrlTest
{
  //@Tested PatientPaged impl;
  //@Mocked JSFUtil util;
  //@Injectable PatientHandler patientHandler;
    @Tested PrescriptionCtrl impl;

    private static Logger LOGGER = Logger.getLogger("javax.faces.component",
                                                    "javax.faces.LogStrings");
    @Test
    public void newPrescription()
    {
        PatientBean pat = new PatientBean();
        impl.newPrescription(pat);
        assertThat(pat.getPrescription(), hasSize(1));
    }

  //@Test 
  //public void cancelEdit()
  //{
  //    impl.startEditPatient();
  //    String result = impl.cancel();

  //    assertFalse(impl.getEditing());
  //}

  //@Test
  //public void saveSelectedPatient_Calls_Handler()
  //{
  //    impl.startEditPatient();
  //    new Expectations(){{
  //        patientHandler.savePatient((PatientBean)any);

  //        util.getCurrentViewId(); result = "";
  //    }};
  //    
  //    PatientBean myPatient = new PatientBean();
  //    myPatient.setForename("Martin");
  //    impl.setSelectedPatient(myPatient);

  //    impl.saveSelectedPatient();
  //    assertFalse(impl.getEditing());
  //}

  //@Test
  //public void createThenCancel()
  //{
  //    impl.startCreatePatient();
  //    assertFalse(impl.getEditing());
  //    assertTrue(impl.getCreating());
  //}

  //@Test
  //public void createSelectedPatient_Calls_Handler()
  //{
  //    new Expectations(){{
  //        patientHandler.deletePatient((PatientBean)any);

  //        util.redirectToSameView(); result = "";
  //    }};
  //    PatientBean myPatient = new PatientBean();
  //    myPatient.setForename("Martin");
  //    impl.setSelectedPatient(myPatient);

  //    impl.deleteSelectedPatient();
  //    assertTrue(impl.getListing());
  //}
}
