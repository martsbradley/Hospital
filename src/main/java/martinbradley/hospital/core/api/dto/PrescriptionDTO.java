package martinbradley.hospital.core.api.dto;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PrescriptionDTO implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionDTO.class);
    private Long id;

    @NotNull
    private PatientDTO patient;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String amount;

    @NotNull
    private MedicineDTO medicine;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public PatientDTO getPatientDTO()
    {
        return patient;
    }

    public void setPatientDTO(PatientDTO patient)
    {
        this.patient = patient;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public MedicineDTO getMedicine()
    {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine)
    {
        this.medicine = medicine;
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("PrescriptionDTO [");
        if (patient != null)
        {
            sb.append("for " );
            sb.append(patient.getForename());
        }
        else
        {
            sb.append("Without Patient!!");
        }
        sb.append("]");
        return sb.toString();
    }
}
