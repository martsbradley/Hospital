package martinbradley.hospital.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> 
{
    //private static final String FORMAT="uuuu-MM-d";
    private static final String FORMAT="yyyy-MM-dd'T'HH:mm'Z'";

    private static DateTimeFormatter FORMATTERS = DateTimeFormatter.ofPattern(FORMAT);

    private static Logger logger = LoggerFactory.getLogger(LocalDateAdapter.class);

    public LocalDate unmarshal(String text) throws Exception {
        LocalDate result =  LocalDate.parse(text,FORMATTERS);

        logger.debug("unmarshal " + text + " returning '" + result);
        return result;
    }

    public String marshal(LocalDate date) throws Exception {

        String text = date.format(FORMATTERS);
        logger.debug("marshal " + date + " returning '" + text);
        return text;
    }
}
