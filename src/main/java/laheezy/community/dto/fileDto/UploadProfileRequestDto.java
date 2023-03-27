package laheezy.community.dto.fileDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadProfileRequestDto {
    private MultipartFile file;
    private String caption;
}
