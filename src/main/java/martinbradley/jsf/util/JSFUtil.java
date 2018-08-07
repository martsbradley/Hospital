package martinbradley.jsf.util;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import martinbradley.hospital.core.api.dto.Message;
import martinbradley.hospital.core.api.dto.MessageCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSFUtil
{
    private static final Logger logger = LoggerFactory.getLogger(JSFUtil.class);
    public Map<String,Object> getSessionMap()
    {
        Map<String,Object> sessionMapObj = getContext().getSessionMap();
        return sessionMapObj;
    }

    public Map<String,Object> getRequestMap()
    {
        Map<String,Object> requestMapObj = getContext().getRequestMap();
        return requestMapObj;
    }

    public Map<String,Object> getViewMap()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,Object> viewMapObj = context.getViewRoot().getViewMap();
        return viewMapObj;
    }

    private ExternalContext getContext()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        ExternalContext ext = context.getExternalContext();
        return ext;
    }

    public String getCurrentViewId()
    {
        String viewId = FacesContext.getCurrentInstance()
                         .getViewRoot().getViewId();
        return viewId;
    }

    public String redirectToSameView()
    {
        JSFUtil jsfUtil = new JSFUtil();
        String viewId = jsfUtil.getCurrentViewId();

        return viewId + "?faces-redirect=true";
    }

    public boolean handleErrors(MessageCollection messages,UIOutput reportErrors)
    {
        if (!messages.hasMessages())
        {
            return false;
        }

        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale locale = ctx.getViewRoot().getLocale();;

        String bundle = "firstcup.web.WebMessages";
        ResourceBundle res = ResourceBundle.getBundle(bundle, locale);

        FacesContext context = FacesContext.getCurrentInstance();

        for (Message m : messages)
        {
            String s = m.getKey();

            try 
            {
                s = res.getString(s);
            }
            catch (java.util.MissingResourceException e)
            {
                String log = "Failed to lookup resource '%s' in message '%s'".
                              format(s,bundle);
                logger.warn(log);
            }

            FacesMessage message = new FacesMessage(s);
            context.addMessage(reportErrors.getClientId(context), message);
        }
        return true;
    }
}
