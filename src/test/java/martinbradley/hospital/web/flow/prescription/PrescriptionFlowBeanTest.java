package martinbradley.hospital.web.flow.prescription;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mockit.*;
import java.util.Arrays;
import java.util.List;
import martinbradley.hospital.web.api.MedicineHandler;

public class PrescriptionFlowBeanTest
{
    @Mocked MedicineHandler medHandler;
    private PrescriptionFlowBean impl= new PrescriptionFlowBean();

    @BeforeEach
    public void setMeUp()
    {
        impl.medHandler = medHandler;
    }

    @Test
    public void medicinesPagedNotNull()
    {
        impl.setSelectedMedicine(null);
    }
}
