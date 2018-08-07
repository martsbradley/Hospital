package martinbradley.hospital.core.api;

import java.util.List;
import martinbradley.hospital.core.domain.Medicine;
import martinbradley.hospital.core.api.dto.MedicineDTO;
import martinbradley.hospital.web.beans.PageInfo;

public interface MedicineBroker
{
    List<MedicineDTO> getMedicinesPaged(PageInfo pageInfo);
    int getTotalMedicines(String filter);
}
