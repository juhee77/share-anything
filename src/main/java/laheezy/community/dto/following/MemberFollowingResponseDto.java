package laheezy.community.dto.following;

import laheezy.community.domain.Following;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberFollowingResponseDto {
    private String nickname;
    private Long userId;

    public MemberFollowingResponseDto toMemberResponseDto(Following following) {
        return new MemberFollowingResponseDto(following.getMemberB().getLoginId(), following.getMemberB().getId());
    }
}
