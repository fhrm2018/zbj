package com.qiyou.dhlive.api.base.outward.vo;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
public class GroupInfo {

    private String GroupId;

    private String Type;

    private Integer MemberNum;

    private List<MemberVO> MemberList;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Integer getMemberNum() {
        return MemberNum;
    }

    public void setMemberNum(Integer memberNum) {
        MemberNum = memberNum;
    }

    public List<MemberVO> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<MemberVO> memberList) {
        MemberList = memberList;
    }
}
