package com.qiyou.dhlive.prd.cron.record.job;

import com.qiyou.dhlive.prd.cron.util.BaseJob;

/**
 * Created by ThinkPad on 2018/3/19.
 */
public class ClearTouristsJob extends BaseJob {
    @Override
    public void preform() {
        clearTourists();
    }

    @Override
    public void setJobName() {
        this.jobName = "清理游客定时任务";
    }

    /**
     * 清理游客
     */
    private void clearTourists() {

    }
}
