package laheezy.community.service;

import laheezy.community.domain.Post;
import laheezy.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post writePost(Post post) {
        //validate
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        return postRepository.save(post);
    }

    public Post findById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new IllegalArgumentException("없는 포스트 아이디 입니다.");
        }
        return post.get();
    }

}
