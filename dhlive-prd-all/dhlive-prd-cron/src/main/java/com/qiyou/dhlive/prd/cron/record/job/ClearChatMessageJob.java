package com.qiyou.dhlive.prd.cron.record.job;

import com.qiyou.dhlive.prd.cron.util.BaseJob;
import com.yaozhong.framework.base.common.utils.DateUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;

import java.util.Date;

/**
 * Created by ThinkPad on 2018/3/19.
 */
public class ClearChatMessageJob extends BaseJob {
    @Override
    public void preform() {
        clearChatMessage();
    }

    @Override
    public void setJobName() {
        this.jobName = "清理聊天记录定时任务";
    }


    /**
     * 清理聊天记录
     */
    private void clearChatMessage() {
        baseLog.info(LogFormatUtil.getActionFormat("缓存黑名单定时任务开始. 时间:" + DateUtil.getDateTime(new Date())));


        baseLog.info(LogFormatUtil.getActionFormat("缓存黑名单定时任务结束. 时间:" + DateUtil.getDateTime(new Date())));
    }
}
