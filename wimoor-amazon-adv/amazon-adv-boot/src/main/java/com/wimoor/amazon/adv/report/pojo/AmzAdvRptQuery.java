package com.wimoor.amazon.adv.report.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.wimoor.amazon.adv.common.pojo.BaseObject;
import com.wimoor.amazon.adv.utils.EmojiFilterUtils;


@Entity
@Table(name="t_amz_adv_rpt_query")
public class AmzAdvRptQuery extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2335373910306161623L;
	@Column(name="query")
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
    	query=EmojiFilterUtils.filterEmoji(query);
        this.query = query;
    }
}