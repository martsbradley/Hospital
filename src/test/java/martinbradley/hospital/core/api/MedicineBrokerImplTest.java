package martinbradley.hospital.core.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mockit.*;
import martinbradley.hospital.persistence.repository.MedicineDBRepo;
import martinbradley.hospital.core.domain.Medicine;
import java.util.Arrays;
import java.util.List;
import martinbradley.hospital.web.beans.PageInfo;
import static martinbradley.hospital.web.beans.PageInfo.PageInfoBuilder;

public class MedicineBrokerImplTest
{
    @Mocked MedicineDBRepo mockRepo;
    private MedicineBrokerImpl impl= new MedicineBrokerImpl();

    @BeforeEach
    public void setMeUp()
    {
        impl.repo = mockRepo;
    }

    @Test
    public void medicinesPagedNotNull()
    {
        PageInfoBuilder builder = new PageInfoBuilder();
        builder.setStartAt(10);
        PageInfo info = builder.build();
        assertNotNull(impl.getMedicinesPaged(info));
    }

  //@Test
  //public void medicines_search_calls_repo()
  //{
  //    impl.searchMedicine("");
  //    new Verifications(){{
  //        mockRepo.searchMedicine(anyString); times = 1;
  //    }};
  //}
}
