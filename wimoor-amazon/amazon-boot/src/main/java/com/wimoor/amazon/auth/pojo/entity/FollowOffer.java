package com.wimoor.amazon.auth.pojo.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amz_follow_offer")
@ApiModel(value="FollowOffer对象", description="FollowOffer")
public class FollowOffer {
	
	@TableField(value = "sellerid")
    private String sellerid;
	
	@TableField(value = "marketplaceid")
	private String marketplaceid;

	@TableField(value = "name")
    private String name;

	@TableField(value = "positive_feedback_rating")
    private Integer positiveFeedbackRating;

	@TableField(value = "feedback_count")
    private Integer feedbackCount;

	@TableField(value = "refreshtime")
    private Date refreshtime;
	
	@TableField(value = "refreshnum")
    private Integer refreshnum;

    
    
}