package martinbradley.hospital.web.beans;
import java.time.LocalDate;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import javax.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlID;
import martinbradley.hospital.jaxb.LocalDateAdapter;

@Named
@ViewScoped
@XmlRootElement(name="patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientBean implements Serializable
{
    @XmlElement
    private Long id;

    @Size(min=1, max=15)
    @XmlElement
    private String forename;

    @Size(min=1, max=20)
    @XmlElement
    private String surname;

    @XmlElement
    private boolean male;

    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate dob = LocalDate.now();

    @XmlElement
    private List<PrescriptionBean> prescription = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(PatientBean.class);

    public PatientBean()
    {
        logger.info("PatientBean was created");
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    public boolean isMale()
    {
        return male;
    }
    public void setMale(boolean male)
    {
        this.male = male;
    }

    public List<PrescriptionBean> getPrescription()
    {
        return prescription;
    }

    public void setPrescription(List<PrescriptionBean> prescription)
    {
        this.prescription = prescription;
    }

    public void addPrescription(PrescriptionBean aPrescription)
    {
        this.prescription.add(aPrescription);
    }

    @Override
    public String toString()
    {
        return "Patient [" + id + ", " 
                           + forename + " " 
                           + surname +   " " 
                           + dob + "]";
    }
    public String addPatient(PatientBean patient)
    {
	logger.info("addPatient(" + patient + ")");
        return "greeting";
    }
}
