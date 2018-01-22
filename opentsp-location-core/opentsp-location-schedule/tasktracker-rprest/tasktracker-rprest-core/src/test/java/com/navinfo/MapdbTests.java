package com.navinfo;


import com.navinfo.tasktracker.rprest.dao.LcDistrictAndTileMappingMapper;
import com.navinfo.tasktracker.rprest.domain.LcDistrictAndTileMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapdbTests {

	/*@Autowired
	private CronJobService cronJobService;

	@Test
	public void contextLoads() throws Exception {

		JobPo re = cronJobService.getJobByTaskId("cleanDialog");
		System.out.printf(JsonUtil.toJson(re));
	}

	@Test
	public void re() throws Exception {
		Boolean re = cronJobService.recoveryJob("cleanDialog");

		System.out.printf("");
	}
	@Test
	public void sus() throws Exception {
		Boolean re = cronJobService.suspendJob("cleanDialog");

		System.out.printf("");
	}

	@Autowired
	private RedisService redisService;

	@Test
	public void redisttest() throws Exception {
		String a= redisService.getStr("abc");
		System.out.println(a);
	}*/
	@Autowired
	private LcDistrictAndTileMappingMapper lcDistrictAndTileMappingMapper;
	@Test
	public void code() throws ParseException {
		DB db = DBMaker.fileDB(new File("mdb.db")).closeOnJvmShutdown().make();
		Map<Long, LcDistrictAndTileMapping> AreaInfoM = db.hashMap("areaInfoMap");

		List<LcDistrictAndTileMapping> areList = lcDistrictAndTileMappingMapper.selectAll();
		System.out.println(areList.size());
		for (LcDistrictAndTileMapping lcDistrictAndTileMapping:areList){
			AreaInfoM.put(lcDistrictAndTileMapping.getTileId(), lcDistrictAndTileMapping);
		}
		db.commit();
		db.close();
		System.out.println("ok");
	}
}
