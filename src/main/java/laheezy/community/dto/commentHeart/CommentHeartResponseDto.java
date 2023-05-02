package laheezy.community.dto.commentHeart;

import laheezy.community.domain.CommentHeart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentHeartResponseDto {
    Long id;
    Long commentId;
    String memberName;
    String date;

    public static CommentHeartResponseDto toConvertPostResponseDto(CommentHeart commentHeart) {
        CommentHeartResponseDto dto = new CommentHeartResponseDto();
        dto.id = commentHeart.getId();
        dto.commentId = commentHeart.getComment().getId();
        dto.memberName = commentHeart.getMember().getNickname();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        dto.date = commentHeart.getDate().format(formatter);
        return dto;
    }
}

