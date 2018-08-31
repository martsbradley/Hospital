package martinbradley.hospital.web.flow.prescription;
import martinbradley.hospital.web.beans.*;

import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import org.primefaces.event.SelectEvent;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import martinbradley.hospital.web.api.MedicineHandler;
import martinbradley.hospital.web.api.PatientHandler;
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
import java.time.LocalDate;
import martinbradley.hospital.web.DateRangeValid;
import martinbradley.hospital.web.LocalDateRange;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Named
@FlowScoped("prescriptionFlow")
@DateRangeValid(groups=java.util.RandomAccess.class)
public class PrescriptionFlowBean implements Serializable, LocalDateRange
{
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionFlowBean.class);
    private static final long serialVersionUID = 1L;
    @Inject MedicineHandler medHandler;
    @Inject PatientHandler patientHandler;

    private Long patientId;
    @NotNull private MedicineBean selectedMedicine;

    @Future
    private LocalDate startDate = LocalDate.now().plusDays(1);
    private LocalDate endDate   = LocalDate.now();

    private String name = "";
    private String medicineSearch;
    private MedicineLazyList medLazyList = new MedicineLazyList();

    @PostConstruct
    public void setHandler()
    {
        logger.debug("PostConstruct called setting " + medHandler);
        medLazyList.setHandler(medHandler);
    }

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
    /**
     * Want methods that execute the validations as the user
     * advances across the pages.
     * gotoStartDate
     * gotoEndDate
     * gotoSummary
     */
    public String gotoStartDate()
    {
        logger.info("gotoStartDate");
        return "setStartDate";
    }

    public String gotoEndDate()
    {
        logger.info("gotoEndDate");
        endDate = startDate.plusDays(1);
        return "setEndDate";
    }

    public String gotoSummary()
    {
        return "summary";
    }

    public String addPrescription()
    {
        PrescriptionBean prescription = new PrescriptionBean();
        prescription.setStartDate(startDate);
        prescription.setEndDate(endDate);
        prescription.setMedicine(selectedMedicine);
        prescription.setAmount("Intravenous");

        logger.info("addPrescription now load the patient " + patientId);
        PatientBean patient = patientHandler.loadById(patientId);

        logger.info("Loaded " + patient);


        List<PrescriptionBean> prescriptions = patient.getPrescription();
        prescriptions.add(prescription);
        patient.setPrescription(prescriptions);

        MessageCollection messages = new MessageCollection();

        long id = patientHandler.savePatient(patient, messages);

        //TODO handle errors.

        return "/patient.xhtml?id="+patientId  +"&faces-redirect=true";
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
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }
}
