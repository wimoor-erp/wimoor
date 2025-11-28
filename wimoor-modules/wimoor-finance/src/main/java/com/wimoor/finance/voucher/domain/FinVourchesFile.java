package com.wimoor.finance.voucher.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 凭证附件对象 fin_vourches_file
 * 
 * @author wimoor
 * @date 2025-11-27
 */
public class FinVourchesFile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 凭证ID */
    @Excel(name = "凭证ID")
    private Long voucherId;

    /** 店铺ID */
    @Excel(name = "店铺ID")
    private String groupid;

    /** 附件路径 */
    @Excel(name = "附件路径")
    private String filePath;

    @Excel(name = "文件名称")
    private String fileName;

    @Excel(name = "文件类型")
    private String fileType;

    /** 操作时间 */
    @Excel(name = "操作时间")
    private Date opttime;

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setVoucherId(Long voucherId)
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId()
    {
        return voucherId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return fileType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("voucherId", getVoucherId())
            .append("groupid", getGroupid())
            .append("filePath", getFilePath())
            .append("fileName", getFileName())
            .append("fileType", getFileType())
            .toString();
    }
}
