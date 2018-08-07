package martinbradley.hospital.web.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import javax.inject.Inject;
import martinbradley.hospital.web.api.MedicineHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import org.primefaces.event.SelectEvent;
import javax.annotation.PostConstruct;

@ViewScoped
@Named
public class MedicinePaged implements Serializable 
{
    private static final Logger logger = LoggerFactory.getLogger(MedicinePaged.class);
    private static final long serialVersionUID = 1L;
    private MedicineLazyList medicineList = new MedicineLazyList();
    @Inject private MedicineHandler medHandler;
    private MedicineBean selectedMedicine;

    public MedicineBean getSelectedMedicine()
    {
        return selectedMedicine;
    }
    public void setSelectedMedicine(MedicineBean selectedMedicine)
    {
        logger.info("setSelectedMedicine " + selectedMedicine);
        this.selectedMedicine = selectedMedicine;
    }
    public LazyDataModel<MedicineBean> getPagedMeds() {
        return medicineList;
    }
    @PostConstruct
    public void setupHandler()
    {
        medicineList.setHandler(medHandler);
    }

    public void onRowSelect(SelectEvent event) 
    {
        logger.info("MedicineLazyList onRowSelect " + event);
        if (event.getObject() != null)
        {
            MedicineBean m = (MedicineBean) event.getObject();
            Long id = m.getId();
            FacesMessage msg = new FacesMessage("MedicineBean Selected", String.valueOf(id));

            setSelectedMedicine( medicineList.getRowData(String.valueOf(id)));

            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
