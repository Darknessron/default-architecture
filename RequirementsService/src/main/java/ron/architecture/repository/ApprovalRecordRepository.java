/**
 * 
 */
package ron.architecture.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import ron.architecture.entity.ApprovalRecord;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 22:57:30
 */
@Component
public interface ApprovalRecordRepository extends PagingAndSortingRepository<ApprovalRecord, Long> {

}
