package laheezy.community.service;

import laheezy.community.domain.Comment;
import laheezy.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Comment writeComment(Comment comment) {
        //validate
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        return commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        if (commentRepository.findById(commentId).isEmpty()) {
            throw new RequestRejectedException("없는 코멘트 입니다.");
        }
        return commentRepository.findById(commentId).get();
    }

    @Transactional
    public void removeComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
