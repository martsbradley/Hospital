package martinbradley.hospital.web.api;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Named;
import martinbradley.hospital.core.api.MedicineBroker;
import martinbradley.hospital.core.api.dto.MedicineDTO;
import martinbradley.hospital.core.domain.Medicine;
import martinbradley.hospital.web.beans.MedicineBean;
import martinbradley.hospital.web.beans.MedicineBeanMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.web.beans.PageInfo;

@ApplicationScoped
@Named
public class MedicineHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MedicineHandler.class);
    final MedicineBeanMapper mapper = Mappers.getMapper(MedicineBeanMapper.class);

    @Inject MedicineBroker medBroker;

    public List<MedicineBean> pageMedicines(PageInfo aPageInfo)
    {
        List<MedicineDTO> list = medBroker.getMedicinesPaged(aPageInfo);
        List<MedicineBean> beans  = convert(list);

        return beans;
    }

    public int getTotalMedicines(String filter)
    {
        return medBroker.getTotalMedicines(filter);
    }

    private List<MedicineBean> convert(List<MedicineDTO> dtoList)
    {
        ArrayList<MedicineBean> beans  = new ArrayList<>();
        for (MedicineDTO p: dtoList)
        {
            MedicineBean bean = mapper.dtoToBean(p);
            beans.add(bean);
        }
        return beans;
    }
    public MedicineBean loadById(long id)
    {
        MedicineDTO dto = medBroker.loadById(id);
        logger.info(String.format("loadById(%d) returned %s",id, dto));
        MedicineBean bean = mapper.dtoToBean(dto);
        return bean;
    }
}
