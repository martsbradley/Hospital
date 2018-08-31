package martinbradley.hospital.web.beans;

import java.io.Serializable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import javax.inject.Inject;
import martinbradley.hospital.web.api.PatientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import org.primefaces.event.SelectEvent;
import martinbradley.jsf.util.JSFUtil;
import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.hospital.core.api.dto.Message;
import javax.faces.component.UIOutput;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.faces.context.FacesContext;

@RequestScoped
@Named
public class PatientCtrl implements Serializable 
{
    private JSFUtil jsfUtil = new JSFUtil();
    private static final Logger logger = LoggerFactory.getLogger(PatientCtrl.class);
    private static final long serialVersionUID = 1L;
    @Inject private PatientHandler patientHandler;
    private PatientBean patientBean = null;

    private UIOutput reportErrors;

    private Long id;

    public Long getId() 
    {
        return id;
    }
    public void setId(Long id) {
        logger.debug("setId "+ id);
        this.id = id;
    }

    public void setPatientBean(PatientBean patientBean)
    {
        this.patientBean = patientBean;
    }

    public PatientBean getPatientBean()
    {
        if (patientBean == null)
        {
            patientBean = new PatientBean();
        }
        return this.patientBean;
    }

    public void load()
    {
        logger.debug("load called id is " + id);
        patientBean = patientHandler.loadById(id);
        logger.debug("loaded "+ patientBean);
    }

    public String saveSelectedPatient(PatientBean patientBean)
    {
        logger.info("saveSelectedPatient(arg) " + patientBean);
        this.patientBean = patientBean;
        return saveSelectedPatient();
    }

    public String saveSelectedPatient()
    {
        logger.info("saveSelectedPatient " + patientBean);
        MessageCollection messages = new MessageCollection();
        long id = patientHandler.savePatient(patientBean, messages);

        logger.info("Saved Patient and got id " + id);

        if (jsfUtil.handleErrors(messages, reportErrors))
        {
            logger.warn("returning null due to errors in service");
            return null;
        }

        String viewId = jsfUtil.getCurrentViewId();

        return "patients" + "?faces-redirect=true";
    }

    public UIOutput getReportErrors()
    {
        return this.reportErrors;
    }
    public void setReportErrors(UIOutput reportErrors)
    {
        this.reportErrors = reportErrors;
    }

    public String startFlow()
    {
        FacesContext.
            getCurrentInstance().
            getExternalContext().
            getFlash().
            put("ThepatientId", this.patientBean.getId());
        return "prescriptionFlow";
    }
}
