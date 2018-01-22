package com.navinfo.tasktracker.lts;

import com.github.ltsopensource.core.domain.Job;
import com.github.ltsopensource.jobclient.domain.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.github.ltsopensource.jobclient.JobClient;

/**
 * 定时任务
 *
 * @author zhangdong
 * @version 1.0
 * @date 2017-7-11
 * @modify
 */
@Service
public class SchedulerService
{
	protected static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
	//demo
	@Value("${task.poiLocation.switch:0/10 * * * * ?}")
	private String poiLocation;

	@Value("${task.delseg.switch:0 0/5 * * * ?}")
	private String delseg;

	@Value("${task.queryCarOnlineCounts.switch:0 0 * * * ?}")
	private String queryCarOnlineCounts;

	@Value("${task.areaVehiclesExecute.switch:0 */1 * * * ?}")
	private String areaVehiclesExecute;

	@Autowired
	private JobClient jobClient;

	/**
	 */
	public void poiLocation()
	{
		try
		{
			Job job = new Job();
			job.setTaskId(Thread.currentThread().getStackTrace()[1].getMethodName());
			job.setParam("description", "密集数据");
			job.setParam("param", "qqi");
			job.setTaskTrackerNodeGroup("qqilocationcloud");
			job.setNeedFeedback(false);
			// 当任务队列中存在这个任务的时候，是否替换更新
			job.setReplaceOnExist(true);
			job.setCronExpression(poiLocation);
			Response response = jobClient.submitJob(job);
			logger.info("");
		} catch (Exception e)
		{
			logger.error("qqilocationcloud poiLocation fail!", e);
		}
	}

	/**
	 */
	public void delSeg()
	{
		try
		{
			Job job = new Job();
			job.setTaskId(Thread.currentThread().getStackTrace()[1].getMethodName());
			job.setParam("description", "删除redis段数据");
			job.setParam("param", "qqSeg");
			job.setTaskTrackerNodeGroup("qqilocationcloud");
			job.setNeedFeedback(false);
			// 当任务队列中存在这个任务的时候，是否替换更新
			job.setReplaceOnExist(true);
			job.setCronExpression(delseg);
			Response response = jobClient.submitJob(job);
			logger.info("");
		} catch (Exception e)
		{
			logger.error("qqilocationcloud poiLocation fail!", e);
		}
	}

	public void queryCarOnlineCounts(){
		try
		{
			Job job = new Job();
			job.setTaskId(Thread.currentThread().getStackTrace()[1].getMethodName());
			job.setParam("description", "获取在线车辆数");
			job.setParam("param", "");
			job.setTaskTrackerNodeGroup("rprest");
			job.setNeedFeedback(false);
			// 当任务队列中存在这个任务的时候，是否替换更新
			job.setReplaceOnExist(true);
			job.setCronExpression(queryCarOnlineCounts);
			Response response = jobClient.submitJob(job);
			logger.info("");
		} catch (Exception e)
		{
			logger.error("rprest queryCarOnlineCounts fail!", e);
		}
	}

	public void areaVehiclesExecute(){
		try
		{
			Job job = new Job();
			job.setTaskId(Thread.currentThread().getStackTrace()[1].getMethodName());
			job.setParam("description", "获取在线车辆数排行");
			job.setParam("param", "");
			job.setTaskTrackerNodeGroup("rprest");
			job.setNeedFeedback(true);
			// 当任务队列中存在这个任务的时候，是否替换更新
			job.setReplaceOnExist(true);
			job.setCronExpression(areaVehiclesExecute);
			Response response = jobClient.submitJob(job);
			logger.info("");
		} catch (Exception e)
		{
			logger.error("rprest areaVehiclesExecute fail!", e);
		}
	}
}
