/**
 * 
 */
package ron.architecture.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 23:03:57
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "REQUIREMENT")
public class Requirement implements Serializable {

	private static final long serialVersionUID = 1080739841485707619L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String category;
	
	@Column(name = "SERIALNUMBER", nullable = false)
	private String serialNumber;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String requester;

	@Column(name = "REQUESTTIME", nullable = false)
	private Date requestTime;
	
	@Column(name = "ISAPPROVE", nullable = false)
	private Boolean isApprove;

	@Column(name = "APPROVALTIME", nullable = true)
	private Date approvalTime;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUIREMENT")
	private List<ApprovalRecord> approvalRecords;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}

	/**
	 * @param requester the requester to set
	 */
	public void setRequester(String requester) {
		this.requester = requester;
	}

	/**
	 * @return the requestTime
	 */
	public Date getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * @return the isApprove
	 */
	public Boolean getIsApprove() {
		return isApprove;
	}

	/**
	 * @param isApprove the isApprove to set
	 */
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}

	/**
	 * @return the approvalTime
	 */
	public Date getApprovalTime() {
		return approvalTime;
	}

	/**
	 * @param approvalTime the approvalTime to set
	 */
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	/**
	 * @return the approvalRecords
	 */
	public List<ApprovalRecord> getApprovalRecords() {
		return approvalRecords;
	}

	/**
	 * @param approvalRecords the approvalRecords to set
	 */
	public void setApprovalRecords(List<ApprovalRecord> approvalRecords) {
		this.approvalRecords = approvalRecords;
	}
	
	
}
