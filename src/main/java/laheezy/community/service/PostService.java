package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post writePost(Post post) {
        //validate
        Post savedPost = postRepository.save(post);
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        return savedPost;
    }

}
