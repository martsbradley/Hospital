package martinbradley.hospital.core.api.dto;

import org.junit.jupiter.api.Test;
import mockit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import martinbradley.hospital.core.domain.UploadedImage;
import martinbradley.hospital.core.domain.Prescription;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class UploadedImageDTOMapperTest
{
    private static final Logger logger = LoggerFactory.getLogger(UploadedImageDTOMapperTest.class);
    @Test
    public void dto_maps_to_patient()
    {
        final UploadedImageDTOMapper mapper = Mappers.getMapper(UploadedImageDTOMapper.class);
        UploadedImageDTO dto = new UploadedImageDTO();

	LocalDateTime myDate = LocalDateTime.now();
        dto.setName("martin");
        dto.setBucket("bradley");
        dto.setPatientId(1L);
        dto.setDescription("m");
	dto.setDateUploaded(myDate);

        UploadedImage image = mapper.dtoToUploadedImage(dto);

        assertEquals("martin",  image.getName());
        assertEquals("bradley", image.getBucket());
        assertEquals("m",       image.getDescription());
        assertEquals(null,      image.getPatient());
        assertEquals(myDate,    image.getDateUploaded());
    }
}
