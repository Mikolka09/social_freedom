package itstep.social_freedom.repository;


import itstep.social_freedom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
