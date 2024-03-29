package laheezy.community.service;

import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static laheezy.community.exception.ErrorCode.ALREADY_FOLLOWING;
import static laheezy.community.exception.ErrorCode.INVALID_FOLLOWING;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;

    @Transactional
    public Following addFollowing(Member memberA, Member memberB) {
        if (checkAlreadyFollowing(memberA, memberB)) {
            throw new CustomException(ALREADY_FOLLOWING);
        }
        return followingRepository.save(new Following(memberA, memberB));
    }

    @Transactional
    public void deleteFollowing(Member memberA, Member memberB) {
        if (!checkAlreadyFollowing(memberA, memberB)) {
            throw new CustomException(INVALID_FOLLOWING);
        }
        Following following = followingRepository.findByMemberAAndMemberB(memberA, memberB).get();
        following.delete();
        followingRepository.delete(following);
    }

    public boolean checkAlreadyFollowing(Member memberA, Member memberB) {
        if (followingRepository.findByMemberAAndMemberB(memberA, memberB).isPresent()) {
            return true;
        }
        return false;
    }

}
