package me.mingshan.util.orm.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hanjuntao
 * @date 2021/8/3
 */
public class PEntity implements Serializable {
    private static final long serialVersionUID = -3996961696364252408L;

    /** 唯一ID */
    private String id;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
