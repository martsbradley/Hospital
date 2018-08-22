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
    private UIOutput reportErrors;
    @Inject private PatientHandler patientHandler;

    public LazyDataModel<PatientBean> getAllPlayers() {
        return patientList;
    }

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
