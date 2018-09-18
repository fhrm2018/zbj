package com.qiyou.dhlive.api.base.outward.vo;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
public class MemberVO {

    private String Member_Account;

    private Long JoinTime;

    private Integer ShuttedUntil;

    public String getMember_Account() {
        return Member_Account;
    }

    public void setMember_Account(String member_Account) {
        Member_Account = member_Account;
    }

    public Integer getShuttedUntil() {
        return ShuttedUntil;
    }

    public void setShuttedUntil(Integer shuttedUntil) {
        ShuttedUntil = shuttedUntil;
    }

    public Long getJoinTime() {
        return JoinTime;
    }

    public void setJoinTime(Long joinTime) {
        JoinTime = joinTime;
    }
}
