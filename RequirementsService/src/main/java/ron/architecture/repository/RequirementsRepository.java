/**
 * 
 */
package ron.architecture.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import ron.architecture.entity.Requirement;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 22:57:30
 */
@Component
public interface RequirementsRepository extends PagingAndSortingRepository<Requirement, Long>, CustomRequirementsRepository {

}
