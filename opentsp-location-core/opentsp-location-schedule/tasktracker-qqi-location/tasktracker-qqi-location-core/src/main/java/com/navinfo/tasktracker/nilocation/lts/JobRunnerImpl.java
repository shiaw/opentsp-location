package com.navinfo.tasktracker.nilocation.lts;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import com.navinfo.tasktracker.nilocation.service.GpsDataSegementThisDay;
import com.navinfo.tasktracker.nilocation.service.PoiDenseLocationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author
 */
@JobRunner4TaskTracker
public class JobRunnerImpl implements JobRunner
{
	private static final Logger logger = LoggerFactory.getLogger(JobRunnerImpl.class);
	@Autowired
	private PoiDenseLocationService poiDenseLocationService;
	@Autowired
	private GpsDataSegementThisDay gpsDataSegementThisDay;

	private final String poiL = "poiLocation";
	private final String segL = "delSeg";


	@Override
	public Result run(JobContext jobContext) throws Throwable
	{
		logger.info("poiLocation JobRunnerImpl:" + jobContext);
		String successjson = null;
		String taskId = jobContext.getJob().getTaskId();
		try
		{
			if (poiL.equals(taskId))
			{
				poiDenseLocationService.poiLocation();
			}
			if(segL.equals(taskId)){
				gpsDataSegementThisDay.delGpsDataSegementThisDay();
			}
			logger.info(successjson);
		} catch (Exception e)
		{
 			logger.error("Run poiLocation failed!", e);
			return new Result(Action.EXECUTE_FAILED, e.getMessage());
		}
		return new Result(Action.EXECUTE_SUCCESS, successjson);
	}
}
