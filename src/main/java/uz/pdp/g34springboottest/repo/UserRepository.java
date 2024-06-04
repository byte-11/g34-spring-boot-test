package uz.pdp.g34springboottest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.g34springboottest.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}