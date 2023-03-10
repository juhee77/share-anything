package laheezy.community.service;

import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import laheezy.community.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowingService {
    private final FollowingRepository followingRepository;

    @Transactional
    public Following addFollowing(Member memberA, Member memberB) {
        if (checkAlreadyFollowing(memberA, memberB)) {
            throw new IllegalArgumentException("이미 팔로우 하셨습니다.");
        }
        return followingRepository.save(new Following(memberA, memberB));
    }

    @Transactional
    public void deleteFollowing(Member memberA, Member memberB) {
        if (!checkAlreadyFollowing(memberA, memberB)) {
            throw new IllegalArgumentException("팔로우 되어있지 않습니다.");
        }
        followingRepository.delete(followingRepository.findByMemberAAndMemberB(memberA, memberB).get());
    }

    public boolean checkAlreadyFollowing(Member memberA, Member memberB) {
        if (followingRepository.findByMemberAAndMemberB(memberA, memberB).isPresent()) {
            return true;
        }
        return false;
    }

}
