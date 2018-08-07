package martinbradley.hospital.web.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.application.FacesMessage;
import org.primefaces.event.SelectEvent;
import javax.faces.context.FacesContext;
import martinbradley.jsf.util.JSFUtil;
import martinbradley.hospital.core.api.dto.MessageCollection;
import martinbradley.hospital.core.api.dto.Message;
import javax.faces.component.UIOutput;
import java.util.ResourceBundle;
import java.util.Locale;

@ViewScoped
@Named
public class PatientPaged implements Serializable 
{
    private JSFUtil jsfUtil = new JSFUtil();
    private static final Logger logger = LoggerFactory.getLogger(PatientPaged.class);
    private static final long serialVersionUID = 1L;
    private PatientLazyList patientList = this.new PatientLazyList();
    @Inject private PatientHandler patientHandler;
    private PatientBean selectedPatient;
    private Status status = Status.LISTING;

    enum Status {
        LISTING,
        EDIT,
        CREATE
    }

    public PatientBean getSelectedPatient()
    {
        return selectedPatient;
    }
    public void setSelectedPatient(PatientBean selectedPatient)
    {
        logger.debug("setSelectedPatient " + selectedPatient);
        this.selectedPatient = selectedPatient;
    }
    public LazyDataModel<PatientBean> getAllPlayers() {
        return patientList;
    }

    public void onRowSelect(SelectEvent event) 
    {
        logger.debug("PatientLazyList onRowSelect " + event);
        if (event.getObject() != null)
        {
            PatientBean m = (PatientBean) event.getObject();
            Long id = m.getId();
            logger.info("onRowSelect " + id);
            FacesMessage msg = new FacesMessage("PatientBean Selected", String.valueOf(id));

            setSelectedPatient( patientList.getRowData(String.valueOf(id)));

            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public String startCreatePatient()
    {
        setSelectedPatient(new PatientBean());
        status = Status.CREATE;
        return "";
    }


    @ManagedProperty(value = "#{patientBean}")
    private PatientBean patientBean;

    public void setPatientBean(PatientBean patientBean)
    {
        this.patientBean = patientBean;
    }
    public PatientBean getPatientBean()
    {
        return this.patientBean;
    }

    public String startEditMyPatient(PatientBean patient)
    {
        logger.info("edit() and selectedPatient = " + selectedPatient);
        PatientBean loadedPatient = patientHandler.loadById(patient.getId());
        logger.info("loadedPatient '" + loadedPatient.getForename() + "'");
        setSelectedPatient(loadedPatient);
        startEditPatient();

        Map<String,Object> requestMap =  jsfUtil.getRequestMap();

        PatientBean found = (PatientBean)requestMap.get("patientBean");

        FacesContext context = FacesContext.getCurrentInstance();
       PatientBean mine = (PatientBean) context.getApplication().
                         evaluateExpressionGet(context, "#{patient}", 
                                  PatientBean.class);

        logger.info("Had ..   " + patient);
        logger.info("Found .. " + found);
        logger.info("View  .. " + patientBean);
        logger.info("mine  .. " + mine);
        return "patient";
    }

    public String startEditPatient()
    {
        status = Status.EDIT;

        return ""; // reload the page
    }

    public String cancel()
    {
        status = Status.LISTING;
        setSelectedPatient(null);
        return "";
    }

    public boolean getListing()
    {
        return status == Status.LISTING;
    }

    public boolean getEditing()
    {
        return status == Status.EDIT;
    }

    public boolean getCreating()
    {
        return status == Status.CREATE;
    }

    public void setEditing(boolean editing)
    {
        if (editing)
            this.status = Status.EDIT;
        else
            this.status = Status.LISTING;
    }
    
    public String deleteSelectedPatient()
    {
        logger.info("why?? deleteSelectedPatient " + selectedPatient);
        status = Status.LISTING;
        patientHandler.deletePatient(selectedPatient);
        return jsfUtil.redirectToSameView();
    }

    private UIOutput reportErrors;
    public UIOutput getReportErrors()
    {
        return this.reportErrors;
    }

    public void setReportErrors(UIOutput reportErrors)
    {
        this.reportErrors = reportErrors;
    }

    class PatientLazyList extends LazyDataModel<PatientBean> 
    {
        private static final long serialVersionUID = 1L;
        private List<PatientBean> players = Collections.emptyList();

        public PatientLazyList()
        {   
            logger.warn("PatientLazyList constructed");
            logger.debug("See debug -------");
            logger.info("DebugEnable " +  logger.isDebugEnabled() + 
                        " InfoEnabled " +  logger.isInfoEnabled() +
                        " WarnEnabled " +  logger.isWarnEnabled());
        }

        @Override
        public List<PatientBean> load(int startingAt, int maxPerPage, 
                            String sortField, SortOrder sortOrder, Map<String, Object> filters) {
            logger.info("Sortfield is " + sortField + " " + sortOrder);
            try {
                players = patientHandler.pagePatients(startingAt,
                                                      maxPerPage,
                                                      sortField, 
                                                      sortOrder == SortOrder.ASCENDING);

                logger.info("PatientLazyList loaded " + players.size() + " objects.");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("PatientLazyList error", e);
            }

            // set the total of 
            if(getRowCount() <= 0){
                setRowCount(patientHandler.getTotalPatients());
            }

            // set the page size
            setPageSize(maxPerPage);

            return players;
        }

        @Override
        public Object getRowKey(PatientBean med) {
            logger.debug("PatientLazyList getRowKey" + med.getId());
            return med.getId();
        }

        @Override
        public PatientBean getRowData(String playerId) {
            Long id = Long.valueOf(playerId);
            logger.debug("getRowData " + id );

            for (PatientBean med : players) {
                if(id.equals(med.getId())){
                    logger.warn("getRowData checking ..med " + med.getId());
                    return med;
                }
            }
            logger.warn("PatientLazyList returning null");
            return null;
        }
    }
}
