package com.qiyou.dhlive.api.base.outward.vo;

/**
 * describe:
 *
 * @author fish
 * @date 2018/01/23
 */
public class ResponseFilterVO {

    private String[] GroupBaseInfoFilter;

    private String[] MemberInfoFilter;

    public String[] getGroupBaseInfoFilter() {
        return GroupBaseInfoFilter;
    }

    public void setGroupBaseInfoFilter(String[] groupBaseInfoFilter) {
        GroupBaseInfoFilter = groupBaseInfoFilter;
    }

    public String[] getMemberInfoFilter() {
        return MemberInfoFilter;
    }

    public void setMemberInfoFilter(String[] memberInfoFilter) {
        MemberInfoFilter = memberInfoFilter;
    }
}
