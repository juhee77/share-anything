package laheezy.community.domain.file;

import jakarta.persistence.*;
import laheezy.community.domain.Member;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = FileType.Purposes.PRO_FILE)
@Setter
public class Profile extends File {
    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public void setMemberProfile() {
        this.member.setProfile(this);
    }

    public Profile makeProfile(Member member, String originName, String imagePath) {
        Profile profile = new Profile();

        profile.setMember(member);
        profile.setMemberProfile();
        profile.setOriginName(originName);
        profile.setStoreName(imagePath);

        return profile;
    }

    @Override
    public MultipartFile toMultiPartFile() {
        return null;
    }
}
