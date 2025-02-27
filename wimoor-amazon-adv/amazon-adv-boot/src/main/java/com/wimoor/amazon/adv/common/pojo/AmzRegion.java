package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_region")
public class AmzRegion  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -841458930870691231L;

	@Id
	@Column(name = "code")
    private String code;

	@Column(name = "name")
    private String name;

	@Column(name = "advname")
    private String advname;

	@Column(name = "advpoint")
    private String advpoint;

	@Column(name = "client_id")
    private String clientId;

	@Column(name = "client_secret")
    private String clientSecret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAdvname() {
        return advname;
    }

    public void setAdvname(String advname) {
        this.advname = advname == null ? null : advname.trim();
    }

    public String getAdvpoint() {
        return advpoint;
    }

    public void setAdvpoint(String advpoint) {
        this.advpoint = advpoint == null ? null : advpoint.trim();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret == null ? null : clientSecret.trim();
    }
    
    
}