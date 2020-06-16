/**
 * 
 */
package ron.architecture.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 23:02:09
 */
@Entity
@DynamicUpdate
@SelectBeforeUpdate
@Table(name = "ROLE")
public class Role implements Serializable {

	private static final long serialVersionUID = -317205315631345833L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(name = "ROLE_ID", nullable = false)
	private String roleId;

	@Column(name = "APPROVALSTAGE", nullable = false)
	private Integer approvalStage;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the approvalStage
	 */
	public Integer getApprovalStage() {
		return approvalStage;
	}

	/**
	 * @param approvalStage the approvalStage to set
	 */
	public void setApprovalStage(Integer approvalStage) {
		this.approvalStage = approvalStage;
	}
	
	
}
