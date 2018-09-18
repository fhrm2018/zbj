package com.qiyou.dhlive.prd.cron.util;

import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.base.common.utils.LogFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseJob {

    protected static Logger baseLog = LoggerFactory.getLogger("baseLog");

    protected String jobName;

    public abstract void preform();

    public abstract void setJobName();

    public void run() {
        setJobName();
        long start = System.currentTimeMillis();
        if (EmptyUtil.isEmpty(jobName))
            jobName = "定时任务";
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--任务开始"));
        preform();
        long end = System.currentTimeMillis();
        baseLog.info(LogFormatUtil.getActionFormat(jobName + "--任务结束----耗时:" + (end - start) + "ms"));
    }
}
