package com.qiyou.dhlive.api.base.outward.vo;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/22
 */
public class AVChatRoomVO {

    private String ActionStatus;

    private String ErrorInfo;

    private Integer ErrorCode;

    private String GroupId;

    private List<GroupInfo> GroupInfo;

    private List<MemberVO> MemberList;

    private List<QueryResult> QueryResult;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        ErrorCode = errorCode;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public List<GroupInfo> getGroupInfo() {
        return GroupInfo;
    }

    public void setGroupInfo(List<GroupInfo> groupInfo) {
        GroupInfo = groupInfo;
    }

    public List<com.qiyou.dhlive.api.base.outward.vo.QueryResult> getQueryResult() {
        return QueryResult;
    }

    public void setQueryResult(List<com.qiyou.dhlive.api.base.outward.vo.QueryResult> queryResult) {
        QueryResult = queryResult;
    }

    public List<MemberVO> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<MemberVO> memberList) {
        MemberList = memberList;
    }
}
