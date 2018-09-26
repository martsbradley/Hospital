package martinbradley.hospital.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> 
{
    private static Logger logger = LoggerFactory.getLogger(LocalDateAdapter.class);

    public LocalDate unmarshal(String text) throws Exception {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
        LocalDate result =  LocalDate.parse(text,formatters);

        logger.debug("unmarshal " + text + " returning '" + result);
        return result;
    }

    public String marshal(LocalDate date) throws Exception {

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
        String text = date.format(formatters);
        logger.debug("marshal " + date + " returning '" + text);
        return text;
    }
}
