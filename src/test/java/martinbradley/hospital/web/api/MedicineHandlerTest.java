package martinbradley.hospital.web.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mockit.*;
import martinbradley.hospital.core.api.dto.MedicineDTO;
import java.util.Arrays;
import java.util.List;
import martinbradley.hospital.core.api.MedicineBroker;
import martinbradley.hospital.web.beans.MedicineBean;
import martinbradley.hospital.web.beans.PageInfo;
import static martinbradley.hospital.web.beans.PageInfo.PageInfoBuilder;

public class MedicineHandlerTest
{
    @Mocked MedicineBroker mockBroker;
    private MedicineHandler impl;

    @BeforeEach
    public void setMeUp() {
        impl = new MedicineHandler();
        impl.medBroker = mockBroker;
    }

    @Test
    public void notNull() {

        PageInfo pageInfo  = pageInfo = getPageInfo();
        assertNotNull(impl.pageMedicines(pageInfo));
    }

    private PageInfo getPageInfo() {
        PageInfo pageInfo;
        pageInfo = new PageInfoBuilder()
                         .setStartAt(0)
                         .setMaxPerPage(1)
                         .setSortField("")
                         .setIsAscending(false)
                         .build();
        return pageInfo;
    }

    @Test
    public void oneMedFound()
    {
        initMedicinesPagedMock(new MedicineDTO());
        PageInfo pageInfo  = pageInfo = getPageInfo();
        assertEquals(1, impl.pageMedicines(pageInfo).size());
    }
    @Test
    public void threeMedsFound()
    {
        initMedicinesPagedMock(new MedicineDTO(), 
                               new MedicineDTO(), 
                               new MedicineDTO());
        PageInfo pageInfo  = pageInfo = getPageInfo();
        assertEquals(3, impl.pageMedicines(pageInfo).size());
    }

    private void initMedicinesPagedMock(MedicineDTO ... meds)
    {
        final List<MedicineDTO> myList = Arrays.asList(meds);
        new Expectations(){{
            mockBroker.getMedicinesPaged((PageInfo)any); 
            result = myList;
        }};
    }
    @Test
    public void correctDetails()
    {
        MedicineDTO med = new MedicineDTO();
        med.setId(44);
        med.setName("Socks");
        med.setManufacturer("Adria");
        initMedicinesPagedMock(med);

        PageInfo pageInfo  = pageInfo = getPageInfo();
        List<MedicineBean> beans = impl.pageMedicines(pageInfo);

        MedicineBean medBean = beans.get(0);
        assertEquals("Socks", medBean.getName());
        assertEquals("Adria", medBean.getManufacturer());
    }
  //public void searchMedicine_calls_broker()
  //{

  //    new Verifications(){{
  //        mockBroker.searchMedicine(anyString); times = 1;
  //    }};
  //}
}
