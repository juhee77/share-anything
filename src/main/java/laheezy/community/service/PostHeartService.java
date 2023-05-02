package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.PostHeart;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.PostHeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static laheezy.community.exception.ErrorCode.ALREADY_POST_HEART;
import static laheezy.community.exception.ErrorCode.INVALID_POST_HEART;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostHeartService {
    private final PostHeartRepository postHeartRepository;

    @Transactional
    public PostHeart addHeart(Member member, Post post) {
        if (checkAlreadyHeart(member, post)) {
            throw new CustomException(ALREADY_POST_HEART);
        }
        return postHeartRepository.save(new PostHeart(member, post));
    }

    @Transactional
    public void deleteHeart(Member member, Post post) {
        if (!checkAlreadyHeart(member, post)) {
            throw new CustomException(INVALID_POST_HEART);
        }
        PostHeart postHeart = postHeartRepository.findByMemberAndPost(member, post).get();
        postHeart.delete(); //연관관계 제거
        postHeartRepository.deleteById(postHeart.getId());
    }

    public boolean checkAlreadyHeart(Member member, Post post) {
        if (postHeartRepository.findByMemberAndPost(member, post).isPresent()) {
            return true;
        }
        return false;
    }

}
