package laheezy.community.dto.postheart;

import laheezy.community.domain.PostHeart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostHeartResponseDto {
    Long id;
    Long postId;
    String memberName;
    String date;

    public static PostHeartResponseDto toConvertPostResponseDto(PostHeart postHeart) {
        PostHeartResponseDto dto = new PostHeartResponseDto();
        dto.id = postHeart.getId();
        dto.postId = postHeart.getPost().getId();
        dto.memberName = postHeart.getMember().getNickname();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        dto.date = postHeart.getDate().format(formatter);
        return dto;
    }
}
