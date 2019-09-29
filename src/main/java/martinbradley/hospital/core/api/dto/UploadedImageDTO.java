package martinbradley.hospital.core.api.dto;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;
public class UploadedImageDTO implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(UploadedImageDTO.class);

    private Long id; // Id of this object in the database

    private long patientId; // Id of patient this image is uploaded for

    @Size(min=5, max=250)
    private String name;

    @Size(min=5, max=40)
    private String bucket;

    @Size(min=0, max=250)
    private String description;

    @NotNull
    private LocalDateTime dateUploaded;

    private MessageCollection messages;

    private byte[] data;

    public void setMessages(MessageCollection messages) {
        this.messages = messages;
    }
   
    public MessageCollection getMessages() {
        if (this.messages == null)
            this.messages = new MessageCollection();
        return this.messages;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public long getPatientId() {
        return patientId;
    }
    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBucket() {
        return bucket;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }
    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "PatientDTO [" + id + ", " 
                              + name + " " 
                              + bucket  + " "
                              + dateUploaded      + "]";
    }
}
