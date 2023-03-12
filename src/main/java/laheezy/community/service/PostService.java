package laheezy.community.service;

import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            throw new IllegalArgumentException("삭제 됐거나 없는 포스트 아이디 입니다.");
        }
        return post.get();
    }

    public List<Post> findFollowPost(Member member) {
        List<Following> following = member.getFollowing();
        List<Post> allPost = new ArrayList<>();
        for (Following following1 : following) {
            List<Post> posts = following1.getMemberB().getPosts();
            allPost.addAll(posts.stream().filter(Post::isOpen).collect(Collectors.toList())); //오픈된것만 받아온다.
        }
        return allPost;
    }
}
