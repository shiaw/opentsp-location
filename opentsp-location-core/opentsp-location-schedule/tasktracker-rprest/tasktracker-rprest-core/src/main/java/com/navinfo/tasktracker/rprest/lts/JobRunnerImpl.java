package com.navinfo.tasktracker.rprest.lts;

import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.core.logger.Logger;
import com.github.ltsopensource.core.logger.LoggerFactory;
import com.github.ltsopensource.spring.boot.annotation.JobRunner4TaskTracker;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;
import com.navinfo.tasktracker.rprest.service.AreaVehiclesCountService;
import com.navinfo.tasktracker.rprest.service.CountCarsService;
import com.navinfo.tasktracker.rprest.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author
 */
@JobRunner4TaskTracker
public class JobRunnerImpl implements JobRunner
{
	private static final Logger logger = LoggerFactory.getLogger(JobRunnerImpl.class);

	private final String queryCarOnlineCounts = "queryCarOnlineCounts";

	private final String areaVehiclesExecute= "areaVehiclesExecute";

	@Autowired
	private CountCarsService countCarsService;

	@Autowired
	private AreaVehiclesCountService areaVehiclesCountService;

	@Override
	public Result run(JobContext jobContext) throws Throwable {
		logger.info("rptest JobRunnerImpl:" + jobContext);
		String successjson = null;
		String taskId = jobContext.getJob().getTaskId();
		try {
			if (queryCarOnlineCounts.equals(taskId)){
				successjson = JsonUtil.toJson(countCarsService.queryCarOnlineCounts());
			} else if (areaVehiclesExecute.equals(taskId)) {
				successjson = JsonUtil.toJson(areaVehiclesCountService.areaVehiclesExecute());
			}
			logger.info(successjson);
		} catch (Exception e) {
 			logger.error("Run poiLocation failed!", e);
			return new Result(Action.EXECUTE_FAILED, e.getMessage());
		}
		return new Result(Action.EXECUTE_SUCCESS, successjson);
	}
}
