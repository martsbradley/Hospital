package martinbradley.hospital.web.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import javax.inject.Inject;
import martinbradley.hospital.web.api.PatientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.application.FacesMessage;
import org.primefaces.event.SelectEvent;
import javax.faces.context.FacesContext;
import martinbradley.jsf.util.JSFUtil;
import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.hospital.core.api.dto.Message;
import javax.faces.component.UIOutput;
import java.util.ResourceBundle;
import java.util.Locale;

@RequestScoped
@Named
public class PrescriptionCtrl implements Serializable 
{
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionCtrl.class);
    private static final long serialVersionUID = 1L;
    private PrescriptionBean prescription;
    private PatientBean patient;

    public PrescriptionBean getPrescription()
    {
        logger.debug("gettting prescription " + prescription);
        return prescription;
    }

    public void setPrescription(PrescriptionBean prescription)
    {
        logger.debug("setting prescription " + prescription);
        this.prescription = prescription;
    }

    public String startEdit(PrescriptionBean prescription)
    {
        setPrescription(prescription);

        logger.info("edit " + prescription);

        return "editPrescription";
    }

    public String newPrescription(PatientBean patient)
    {
        this.patient = patient;
        prescription= new PrescriptionBean();
        patient.addPrescription(prescription);
        return "editPrescription";
    }
}
