/**
 * 
 */
package ron.architecture.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 23:04:30
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "APPROVALRECORD")
public class ApprovalRecord implements Serializable {
	
	private static final long serialVersionUID = -1510206455982898319L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REQUIREMENT", referencedColumnName = "ID")
	private Requirement requirement;
	
	@Column(name = "CURRENTSTAGE", nullable = false)
	private Integer currentStage;
	
	@Column(name = "NEXTSTAGE", nullable = true)
	private Integer nextStage;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REQUESTER", referencedColumnName = "ACCOUNT", nullable = false)
	private User requester;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "APPROVER", referencedColumnName = "ACCOUNT", nullable = true)
	private User approver;

	@Column(name = "RECORDTIME", nullable = false)
	private Date recordTime;

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
	 * @return the requirement
	 */
	public Requirement getRequirement() {
		return requirement;
	}

	/**
	 * @param requirement the requirement to set
	 */
	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	/**
	 * @return the currentStage
	 */
	public Integer getCurrentStage() {
		return currentStage;
	}

	/**
	 * @param currentStage the currentStage to set
	 */
	public void setCurrentStage(Integer currentStage) {
		this.currentStage = currentStage;
	}

	/**
	 * @return the nextStage
	 */
	public Integer getNextStage() {
		return nextStage;
	}

	/**
	 * @param nextStage the nextStage to set
	 */
	public void setNextStage(Integer nextStage) {
		this.nextStage = nextStage;
	}

	/**
	 * @return the requester
	 */
	public User getRequester() {
		return requester;
	}

	/**
	 * @param requester the requester to set
	 */
	public void setRequester(User requester) {
		this.requester = requester;
	}

	/**
	 * @return the approver
	 */
	public User getApprover() {
		return approver;
	}

	/**
	 * @param approver the approver to set
	 */
	public void setApprover(User approver) {
		this.approver = approver;
	}

	/**
	 * @return the recordTime
	 */
	public Date getRecordTime() {
		return recordTime;
	}

	/**
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	

}
