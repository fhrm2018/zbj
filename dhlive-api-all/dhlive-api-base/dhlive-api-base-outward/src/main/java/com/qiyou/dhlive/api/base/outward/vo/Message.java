package com.qiyou.dhlive.api.base.outward.vo;

import java.util.List;

/**
 * describe:
 *
 * @author fish
 * @date 2018/02/03
 */
public class Message {

	private String GroupId;

	private Integer Random;

	private Integer SyncOtherMachine;

	private String From_Account;

	private String To_Account;

	private Integer MsgRandom;

	private String UniqueId;

	private Integer isAudit;

	public Integer getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}

	public String getUniqueId() {
		return UniqueId;
	}

	public void setUniqueId(String uniqueId) {
		UniqueId = uniqueId;
	}

	private List<MsgBody> MsgBody;

	public Integer getSyncOtherMachine() {
		return SyncOtherMachine;
	}

	public void setSyncOtherMachine(Integer syncOtherMachine) {
		SyncOtherMachine = syncOtherMachine;
	}

	public String getFrom_Account() {
		return From_Account;
	}

	public void setFrom_Account(String from_Account) {
		From_Account = from_Account;
	}

	public String getTo_Account() {
		return To_Account;
	}

	public void setTo_Account(String to_Account) {
		To_Account = to_Account;
	}

	public Integer getMsgRandom() {
		return MsgRandom;
	}

	public void setMsgRandom(Integer msgRandom) {
		MsgRandom = msgRandom;
	}

	public List<com.qiyou.dhlive.api.base.outward.vo.MsgBody> getMsgBody() {
		return MsgBody;
	}

	public void setMsgBody(List<com.qiyou.dhlive.api.base.outward.vo.MsgBody> msgBody) {
		MsgBody = msgBody;
	}

	public String getGroupId() {
		return GroupId;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

	public Integer getRandom() {
		return Random;
	}

	public void setRandom(Integer random) {
		Random = random;
	}
}
