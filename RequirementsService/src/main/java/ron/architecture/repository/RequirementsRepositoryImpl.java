/**
 * 
 */
package ron.architecture.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ron.architecture.RequirementsServiceApplication;
import ron.architecture.entity.ApprovalRecord;
import ron.architecture.entity.Requirement;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-17 21:03:50
 */
public class RequirementsRepositoryImpl implements CustomRequirementsRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public JsonNode findRequirement(String category, String serialNumber, String subject, String requester,
			Date requestStartTime, Date requestEndstartTime, Integer pageSize, Integer pageNumber) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Requirement> query = builder.createQuery(Requirement.class);
		Root<Requirement> requirement = query.from(Requirement.class);
		requirement.alias("r");
		Predicate predicates = builder.conjunction();
		
		if (!StringUtils.isEmpty(category))	{
			predicates = builder.and(predicates, builder.equal(requirement.get("category"), category));
		}
		
		if (!StringUtils.isEmpty(serialNumber))	{
			predicates = builder.and(predicates, builder.like(requirement.get("serialNumber"), serialNumber + "%"));
		}
		
		if (!StringUtils.isEmpty(subject))	{
			predicates = builder.and(predicates, builder.like(requirement.get("subject"), "%"+serialNumber + "%"));
		}
		
		if (!StringUtils.isEmpty(requester))	{
			predicates = builder.and(predicates, builder.equal(requirement.get("requester"), requester));
		}
		
		if (requestStartTime != null)	{
			predicates = builder.and(predicates, builder.greaterThanOrEqualTo(requirement.get("requestTime"), requestStartTime));
		}
		
		if (requestEndstartTime != null)	{
			predicates = builder.and(predicates, builder.lessThanOrEqualTo(requirement.get("requestTime"), requestEndstartTime));
		}
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		countQuery.from(query.getResultType()).alias("r");
		countQuery.select(builder.count(requirement)).where(predicates);
		
		Long size = entityManager.createQuery(countQuery).getSingleResult();
		query.select(requirement).where(predicates).orderBy(builder.asc(requirement.get("requestTime")));

		TypedQuery<Requirement> typedQuery = entityManager.createQuery(query);
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode root = objectMapper.createObjectNode();
		
		ObjectNode pagination = objectMapper.createObjectNode();
			
		pagination.put("totlal", size);
		if (pageSize != null && pageSize.intValue() > 0) {
			pagination.put("pageSize", pageSize);
			typedQuery.setMaxResults(pageSize);
		}
		if (pageNumber != null && pageNumber.intValue() > 0) {
			pagination.put("pageNumber", pageNumber);
			typedQuery.setFirstResult(pageNumber - 1);
		}
		root.set("pagination", pagination);
		
		ArrayNode requirements = objectMapper.createArrayNode();
		
		List<Requirement> list = typedQuery.getResultList();
		list.stream().forEach(r -> {
			requirements.add(copyEntityToObjectNode(r));
		});
		
		root.set("result", requirements);
		return root;
	}

	
	private ObjectNode copyEntityToObjectNode(Requirement requirement)	{
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("SerialNumber", requirement.getSerialNumber());
		node.put("Category", requirement.getCategory());
		node.put("Subject", requirement.getSubject());
		node.put("Content", requirement.getContent());
		node.put("Requester", requirement.getRequester());
		node.put("RequestTime", RequirementsServiceApplication.sdf.format(requirement.getRequestTime()));
		boolean isApprove = requirement.getIsApprove();
		node.put("IsApprove", isApprove);
		if (isApprove) {
			node.put("ApprovalTime", RequirementsServiceApplication.sdf.format(requirement.getApprovalTime()));
			ArrayNode approvaRecordNode = objectMapper.createArrayNode();
			requirement.getApprovalRecords().stream().forEach(r ->{
				approvaRecordNode.add(copyEntityToObjectNode(r));
			});
		}
		return node;
	}
	
	private ObjectNode copyEntityToObjectNode(ApprovalRecord record)	{
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("Approver", record.getApprover().getName());
		return node;
	}

}
