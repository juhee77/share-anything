package laheezy.community.service;

import laheezy.community.domain.User;
import laheezy.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long saveUser(String name) {
        User user = User.makeUser(name);
        userRepository.save(user);
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        return user.getId();
    }

}
