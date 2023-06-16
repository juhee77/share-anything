package laheezy.community.service;

import laheezy.community.domain.Comment;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        if (commentRepository.findById(commentId).isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_COMMENT);
        }
        return commentRepository.findById(commentId).get();
    }

    @Transactional
    public void removeComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Comment modify(Comment savedComment, Comment comment) {
        savedComment.modify(comment);
        return savedComment;
    }
}
