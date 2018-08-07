package martinbradley.hospital.core.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Inject;
import martinbradley.hospital.core.domain.Medicine;
import martinbradley.hospital.persistence.repository.MedicineDBRepo;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Model;
import org.mapstruct.factory.Mappers;
import martinbradley.hospital.core.api.dto.MedicineDTO;
import martinbradley.hospital.core.api.dto.MedicineDTOMapper;
import martinbradley.hospital.web.beans.PageInfo;

@Model
public class MedicineBrokerImpl implements MedicineBroker
{
    @Inject MedicineDBRepo repo;
    private final static MedicineDTOMapper mapper = Mappers.getMapper(MedicineDTOMapper.class);

    @Override
    public List<MedicineDTO> getMedicinesPaged(PageInfo pageInfo)
    {
        Medicine.SortOrder ordering = Medicine.SortOrder.find(pageInfo.getSortField(), 
                                                              pageInfo.isAscending());

        List<Medicine> meds = repo.pageMedicines(pageInfo.getStartingAt(), 
                                                 pageInfo.getMaxPerPage(), 
                                                 ordering,
                                                  pageInfo.getFilter());

        List<MedicineDTO> dtoList = convert(meds);

        return dtoList;
    }

    public int getTotalMedicines(String filter)
    {
        return repo.getTotalMedicines(filter);
    }

    private List<MedicineDTO> convert(List<Medicine> meds)
    {
        List<MedicineDTO> dtoList = new ArrayList<>();

        for (Medicine med: meds)
        {
            MedicineDTO dto = mapper.medicineToDTO(med);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
