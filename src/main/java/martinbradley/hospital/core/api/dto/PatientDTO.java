package martinbradley.hospital.core.api.dto;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import java.util.List;
public class PatientDTO implements Serializable
{
    private long id;
    @Size(min=1, max=15)
    private String forename;

    @Size(min=1, max=20)
    private String surname;

    @NotNull
    private String sex;

    @NotNull
    private LocalDate dob;
    private static final Logger logger = LoggerFactory.getLogger(PatientDTO.class);

    private MessageCollection messages;
    private List<PrescriptionDTO> prescription;

    public List<PrescriptionDTO> getPrescription()
    {
        return prescription;
    }

    public void setPrescription(List<PrescriptionDTO> prescription)
    {
        this.prescription = prescription;
    }

    public void setMessages(MessageCollection messages)
    {
        this.messages = messages;
    }
   
    public MessageCollection getMessages()
    {
        if (this.messages == null)
            this.messages = new MessageCollection();
        return this.messages;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getForename() {
        return forename;
    }
    public void setForename(String forename) {
        this.forename = forename;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public String getSex()
    {
        return sex;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    @Override
    public String toString()
    {
        return "PatientDTO [" + id + ", " 
                              + forename + " " 
                              + surname  + " "
                              + dob      + "]";
    }
}
