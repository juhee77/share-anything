package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.PostHeart;
import laheezy.community.repository.PostHeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostHeartService {
    private final PostHeartRepository postHeartRepository;

    @Transactional
    public PostHeart addHeart(Member member, Post post) {
        if (checkAlreadyHeart(member, post)) {
            throw new IllegalArgumentException("이미 좋아요 된 게시글 입니다");
        }
        return postHeartRepository.save(new PostHeart(member, post));
    }

    @Transactional
    public void deleteHeart(Member member, Post post) {
        if (!checkAlreadyHeart(member, post)) {
            throw new IllegalArgumentException("좋아요 하지 않은 게시글 입니다");
        }
        postHeartRepository.deleteById(postHeartRepository.findByMemberAndPost(member, post).get().getId());
    }

    public boolean checkAlreadyHeart(Member member, Post post) {
        if (postHeartRepository.findByMemberAndPost(member, post).isPresent()) {
            return true;
        }
        return false;
    }

}
