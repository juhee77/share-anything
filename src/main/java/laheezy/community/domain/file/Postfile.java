package laheezy.community.domain.file;

import jakarta.persistence.*;
import laheezy.community.domain.Post;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = FileType.Purposes.PRO_FILE)
@Setter
public class Postfile extends File {
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;


    public void setPostImage() {
        this.post.getPostfiles().add(this);
    }


    public Postfile makePostfile(Post post, String caption, String originName, String imagePath) {
        Postfile postImg = new Postfile();
        if (caption != null) postImg.setCaption(caption);
        postImg.setPost(post);
        postImg.setPostImage();
        postImg.setOriginName(originName);
        postImg.setStoreName(imagePath);

        return postImg;
    }


}
