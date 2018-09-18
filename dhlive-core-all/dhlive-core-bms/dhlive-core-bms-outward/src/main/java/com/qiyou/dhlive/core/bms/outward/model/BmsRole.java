package com.qiyou.dhlive.core.bms.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bms_role")
public class BmsRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态,1 正常 2 停用
     */
    private Integer status;

    /**
     * 更新人
     */
    @Column(name = "modify_user_id")
    private Integer modifyUserId;

    /**
     * 更新时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 添加人
     */
    @Column(name = "create_user_id")
    private Integer createUserId;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取状态,1 正常 2 停用
     *
     * @return status - 状态,1 正常 2 停用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态,1 正常 2 停用
     *
     * @param status 状态,1 正常 2 停用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取更新人
     *
     * @return modify_user_id - 更新人
     */
    public Integer getModifyUserId() {
        return modifyUserId;
    }

    /**
     * 设置更新人
     *
     * @param modifyUserId 更新人
     */
    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    /**
     * 获取更新时间
     *
     * @return modify_time - 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取添加人
     *
     * @return create_user_id - 添加人
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * 设置添加人
     *
     * @param createUserId 添加人
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}