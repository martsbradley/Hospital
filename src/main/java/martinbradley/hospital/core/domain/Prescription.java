package martinbradley.hospital.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="prescription")
public class Prescription
{
    @Id 
    @SequenceGenerator(name="prescription_pk_sequence",sequenceName="prescription_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="prescription_pk_sequence")
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="medicine_id")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(name="amount")
    private String amount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient(Patient patient)
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

    public Medicine getMedicine()
    {
        return medicine;
    }

    public void setMedicineId(Medicine medicine)
    {
        this.medicine = medicine;
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription [");
        sb.append(id);
        sb.append(",");
        sb.append(startDate);
        sb.append(",");
        sb.append(endDate);
        sb.append(",");
        sb.append(amount);
        if (patient != null)
        {
            sb.append(", patient forename");
            sb.append(patient.getForename());
        }
        else
        {
            sb.append(" patient is null");
        }

        sb.append("]");
        return sb.toString();
    }
}
