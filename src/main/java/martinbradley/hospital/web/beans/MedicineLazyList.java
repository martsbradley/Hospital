package martinbradley.hospital.web.beans;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Collections;
import martinbradley.hospital.web.api.MedicineHandler;
import java.util.Map;
import static martinbradley.hospital.web.beans.PageInfo.PageInfoBuilder;

public class MedicineLazyList extends LazyDataModel<MedicineBean> 
{
    private static final Logger logger = LoggerFactory.getLogger(MedicineLazyList.class);
    private static final long serialVersionUID = 1L;
    private List<MedicineBean> medicines = Collections.emptyList();
    private MedicineHandler medHandler;
    private String filter = "";

    public void setHandler(MedicineHandler medHandler)
    {
        this.medHandler = medHandler;
        logger.debug("setHandler " + medHandler);
    }

    @Override
    public List<MedicineBean> load(int startingAt, 
                                   int maxPerPage, 
                                   String sortField, 
                                   SortOrder sortOrder, 
                                   Map<String, Object> filters) 
    {
        logger.info("Sortfield is " + sortField + " " + sortOrder);

        PageInfo pageInfo  = new PageInfoBuilder()
                                 .setStartAt(startingAt)
                                 .setMaxPerPage(maxPerPage)
                                 .setSortField(sortField)
                                 .setIsAscending(sortOrder == SortOrder.ASCENDING)
                                 .setFilter(filter)
                                 .build();
        try {
            medicines = medHandler.pageMedicines(pageInfo);
            logger.info("PlayerLazyList loaded " + medicines.size() + " objects.");
        } catch (Exception e) 
        {
            e.printStackTrace();
            logger.info("PlayerLazyList error", e);
        }

        // if (filterchanged_checker...) //total only changes sometimes!
        // so don't call it all the time.
        {
            setRowCount(medHandler.getTotalMedicines(filter));
        }

        // set the page size
        setPageSize(maxPerPage);

        return medicines;
    }

    @Override
    public Object getRowKey(MedicineBean med) {
        logger.debug("MedicineLazyList getRowKey" + med.getId());
        return med.getId();
    }

    @Override
    public MedicineBean getRowData(String playerId) {
        Long id = Long.valueOf(playerId);

        for (MedicineBean med : medicines) {
            if(id.equals(med.getId())){
                logger.debug("getRowData checking ..med " + med.getId());
                return med;
            }
        }
        logger.debug("MedicineLazyList returning null");
        return null;
    }

    public void setFilter(String medicineSearch)
    {
        this.filter = medicineSearch;
    }
}
