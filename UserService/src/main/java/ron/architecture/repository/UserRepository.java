/**
 * 
 */
package ron.architecture.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import ron.architecture.entity.User;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 22:57:30
 */
@Component
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	public Optional<User> findByAccountAndPassword(String account, String password);
}
