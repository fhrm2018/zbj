package com.qiyou.dhlive.core.base.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "base_sys_param")
public class BaseSysParam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 参数的健
     */
    @Column(name = "param_key")
    private String paramKey;

    /**
     * 参数的值
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * 类型 1微信相关配置  2服务相关配置
     */
    private Integer type;

    /**
     * 状态,0失效,1正常
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取参数的健
     *
     * @return param_key - 参数的健
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * 设置参数的健
     *
     * @param paramKey 参数的健
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取参数的值
     *
     * @return param_value - 参数的值
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * 设置参数的值
     *
     * @param paramValue 参数的值
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * 获取类型 1微信相关配置  2服务相关配置
     *
     * @return type - 类型 1微信相关配置  2服务相关配置
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型 1微信相关配置  2服务相关配置
     *
     * @param type 类型 1微信相关配置  2服务相关配置
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取状态,0失效,1正常
     *
     * @return status - 状态,0失效,1正常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态,0失效,1正常
     *
     * @param status 状态,0失效,1正常
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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