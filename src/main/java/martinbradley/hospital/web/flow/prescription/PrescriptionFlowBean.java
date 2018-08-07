package martinbradley.hospital.web.flow.prescription;

import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import org.primefaces.event.SelectEvent;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import martinbradley.hospital.web.api.MedicineHandler;
import martinbradley.hospital.web.beans.MedicineBean;
import java.util.List;
import java.util.Collections;
import javax.inject.Inject;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import martinbradley.hospital.web.beans.MedicineLazyList;
import org.primefaces.model.LazyDataModel;
import javax.annotation.PostConstruct;
import javax.validation.constraints.*;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.util.Set;
import javax.faces.component.UIOutput;
import martinbradley.hospital.core.api.dto.ConstraintToMessageConverter;
import martinbradley.hospital.core.api.dto.Message;
import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.jsf.util.JSFUtil;
import java.time.LocalDate;
/**
 * Backing bean for CheckoutFlow.
 */
@Named
@FlowScoped("prescriptionFlow")
public class PrescriptionFlowBean implements Serializable 
{
    private JSFUtil jsfUtil = new JSFUtil();
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionFlowBean.class);
    private static final long serialVersionUID = 1L;

    private Long patientId;
    @NotNull private MedicineBean selectedMedicine;

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

    private String name = "";
    private String medicineSearch;
    @Inject MedicineHandler medHandler;
    private MedicineLazyList medLazyList = new MedicineLazyList();

    public void setSelectedMedicine(MedicineBean selectedMedicine)
    {
        if (selectedMedicine == null)
        {
            logger.warn("setSelectedMedicine called with null, taking no action");
        }
        else
        {
            this.selectedMedicine = selectedMedicine;
            name = selectedMedicine.getName();
        }
    }

    public MedicineBean getSelectedMedicine()
    {
        return this.selectedMedicine;
    }

    public void onMedicineSelect(SelectEvent event) 
    {
        Long id = ((MedicineBean) event.getObject()).getId();
        logger.warn("onRowSelect got row " + id);

        MedicineBean med = medLazyList.getRowData(id.toString());
        setSelectedMedicine(med);
    }

    @PostConstruct
    public void setHandler()
    {
        logger.debug("PostConstruct called setting " + medHandler);
        medLazyList.setHandler(medHandler);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicineSearch() {
        return medicineSearch;
    }

    public void setMedicineSearch(String medicineSearch) {
        this.medicineSearch = medicineSearch;
    }

    public Long getPatientId()
    {
        return patientId;
    }

    public void startFlow()
    {
        logger.debug("Start Flow called");
        patientId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("ThepatientId");
        logger.debug("Start Flow got " + patientId);
    }

    public String getCancel()
    {
        logger.debug("Cancel!");
        FacesContext.getCurrentInstance().
                     getExternalContext().
                     getFlash().
                     put("ThepatientId", patientId);

        String to = "cancelFlow.xhtml";
        logger.debug("sending to '" + to + "'");

        return to;
    }

    public String goStartDate()
    {
        logger.debug("goStartDate");

      //ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      //Validator validator = factory.getValidator();
      //Set<ConstraintViolation<PrescriptionFlowBean>> violations = validator.validate(this);

      //logger.debug("Validation found " + violations.size());

///     MessageCollection messages = new MessageCollection();
///     
///     if (!violations.isEmpty())
///     {
///         ConstraintToMessageConverter conv = new ConstraintToMessageConverter();

///         for (ConstraintViolation<PrescriptionFlowBean> c : violations)
///         {
///             Message message = conv.getMessage(c);
///             messages.add(message);
///         }
///     }

      //if (jsfUtil.handleErrors(messages, reportErrors))
      //{
      //    return null;
      //}

        return "setStartDate";
    }

    public void doneFinished()
    {
        logger.debug("Flow finished");
    }

    public String searchMedicines()
    {
        setSelectedMedicine(null);
        logger.warn("Searching for '" + medicineSearch + "'");
        medLazyList.setFilter(medicineSearch);
        return "";
    }

    public MedicineLazyList getPagedMeds()
    {

        return medLazyList;
    }
    /*
    private UIOutput reportErrors;
    public UIOutput getReportErrors()
    {
        return this.reportErrors;
    }
    public void setReportErrors(UIOutput reportErrors)
    {
        this.reportErrors = reportErrors;
    }
    */
    public LocalDate getStartDate()
    {
        logger.warn("getStartDate called " + startDate);
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        logger.warn("setStartDate called " + startDate);
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        logger.warn("getEnd called " + endDate);
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        logger.warn("setStartDate called " + endDate);
        this.endDate = endDate;
    }
}
