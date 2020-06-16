/**
 * 
 */
package ron.architecture.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import ron.architecture.entity.Role;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 22:57:30
 */
@Component
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

}
