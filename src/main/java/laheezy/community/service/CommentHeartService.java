package laheezy.community.service;

import laheezy.community.domain.Comment;
import laheezy.community.domain.CommentHeart;
import laheezy.community.domain.Member;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.repository.CommentHeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentHeartService {
    private final CommentHeartRepository CommentHeartRepository;

    @Transactional
    public CommentHeart addHeart(Member member, Comment comment) {
        if (checkAlreadyHeart(member, comment)) {
            throw new CustomException(ErrorCode.ALREADY_COMMENT_HEART);
        }
        return CommentHeartRepository.save(new CommentHeart(member, comment));
    }

    @Transactional
    public void deleteHeart(Member member, Comment comment) {
        if (!checkAlreadyHeart(member, comment)) {
            throw new CustomException(ErrorCode.INVALID_COMMENT_HEART);
        }
        CommentHeartRepository.deleteById(CommentHeartRepository.findByMemberAndComment(member, comment).get().getId());
    }

    public boolean checkAlreadyHeart(Member member, Comment comment) {
        return CommentHeartRepository.findByMemberAndComment(member, comment).isPresent();
    }

}
