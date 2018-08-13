package martinbradley.hospital.web.beans;
import java.time.LocalDate;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import javax.validation.constraints.*;

@Named
@RequestScoped
public class PrescriptionBean implements Serializable
{
    private long id;
    private LocalDate startDate, endDate;
    private String amount;
    private MedicineBean medicine;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public void setMedicine(MedicineBean medicine)
    {
        this.medicine = medicine;
    }

    public MedicineBean getMedicine()
    {
        return this.medicine;
    }
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PrescriptionBean [");
        sb.append(" Id:");
        sb.append(this.id);
        sb.append(" Start Date:");
        sb.append(this.startDate);
        sb.append(" Amount:");
        sb.append(this.amount);
        sb.append("]");
        return sb.toString();
    }
}