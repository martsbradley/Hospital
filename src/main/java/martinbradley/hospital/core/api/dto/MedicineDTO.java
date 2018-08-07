package martinbradley.hospital.core.api.dto;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import javax.validation.constraints.*;

public class MedicineDTO implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(MedicineDTO.class);

    private long id;
    @Size(min=1, max=15)
    private String name;

    @Size(min=1, max=20)
    private String manufacturer;

    @NotNull
    private String deliveryMethod;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getDeliveryMethod()
    {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod)
    {
        this.deliveryMethod = deliveryMethod;
    }
}
