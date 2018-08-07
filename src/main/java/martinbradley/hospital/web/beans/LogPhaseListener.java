package martinbradley.hospital.web.beans;

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
import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Iterator;

public class LogPhaseListener implements PhaseListener
{
    private static final Logger logger = LoggerFactory.getLogger(LogPhaseListener.class);
    @Override
    public void afterPhase(PhaseEvent event){
        logger.info("AfterPhase " + event.getPhaseId());
        FacesContext context = event.getFacesContext();
        Iterator<FacesMessage> it = context.getMessages();
        for (;it.hasNext();)
        {
            FacesMessage m = it.next();
            logger.info(m.getDetail());
        }
    }
    @Override
    public void beforePhase(PhaseEvent event){
        logger.info("beforePhase " + event.getPhaseId());
    }

    @Override
    public PhaseId getPhaseId(){
        return PhaseId.ANY_PHASE;
    }
}
