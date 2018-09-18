package com.qiyou.dhlive.core.base.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "base_ip")
public class BaseIp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ip;

    /**
     * 0黑名单, 1白名单
     */
    private Integer type;

    @Column(name = "create_user_id")
    private Integer createUserId;

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
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取0黑名单, 1白名单
     *
     * @return type - 0黑名单, 1白名单
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置0黑名单, 1白名单
     *
     * @param type 0黑名单, 1白名单
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return create_user_id
     */
    public Integer getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     */
    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}