package com.qiyou.dhlive.core.bms.outward.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "bms_resource")
public class BmsResource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单url
     */
    private String url;

    /**
     * 类型，1 一级菜单 2 二级菜单
     */
    private Integer type;

    /**
     * 排序码
     */
    private Integer sort;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "parent_name")
    private String parentName;

    @Column(name = "group_name")
    private String groupName;

    /**
     * 状态,1 正常 2 停用
     */
    private Integer status;

    /**
     * 说明
     */
    private String remark;

    /**
     * 图标样式
     */
    private String icon;

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
     * 获取菜单url
     *
     * @return url - 菜单url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置菜单url
     *
     * @param url 菜单url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取类型，1 一级菜单 2 二级菜单
     *
     * @return type - 类型，1 一级菜单 2 二级菜单
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型，1 一级菜单 2 二级菜单
     *
     * @param type 类型，1 一级菜单 2 二级菜单
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取排序码
     *
     * @return sort - 排序码
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序码
     *
     * @param sort 排序码
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return parent_id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return parent_name
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return group_name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
     * 获取说明
     *
     * @return remark - 说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置说明
     *
     * @param remark 说明
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取图标样式
     *
     * @return icon - 图标样式
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标样式
     *
     * @param icon 图标样式
     */
    public void setIcon(String icon) {
        this.icon = icon;
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