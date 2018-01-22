package com.navinfo.tasktracker.lts;

import com.github.ltsopensource.core.commons.utils.CollectionUtils;
import com.github.ltsopensource.core.domain.JobResult;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.jobclient.support.JobCompletedHandler;
import com.github.ltsopensource.spring.boot.annotation.JobCompletedHandler4JobClient;

import java.util.List;

/**
 * @author zhangdong
 */
// 增加这个注解, 即可作为任务执行结果反馈回调接口(也可以不使用)
@JobCompletedHandler4JobClient
public class JobCompletedHandlerImpl implements JobCompletedHandler
{
	protected static final Logger logger = LoggerFactory.getLogger(JobCompletedHandlerImpl.class);

	@Override
	public void onComplete(List<JobResult> jobResults)
	{
		// 任务执行反馈结果处理
		if (CollectionUtils.isNotEmpty(jobResults))
		{
			for (JobResult jobResult : jobResults)
			{
				logger.info("job comleted:" + jobResult);
				String rejson = jobResult.getMsg();
//				SuccessResponse httpCommandResultWithData = JsonUtil.fromJson(rejson, SuccessResponse.class);
			}
		}
	}
}
