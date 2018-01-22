import com.navinfo.opentsp.platform.da.core.Application;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xubh on 2017/4/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RedisTest {

    @Test
    public void testGetTerminalFirstRecieveDate() {
        RedisStatic.getInstance();
        TerminalOnOffStatusServiceImpl onOffStatus = new TerminalOnOffStatusServiceImpl();
        List<Long> ids = new ArrayList<>();
        ids.add(Long.valueOf("20000145101"));
        Map<Long, Long> statusMap = onOffStatus.getTerminalFirstRecieveDate(ids);
        Assert.assertEquals(1, statusMap.size());
    }

    @Test
    public void testDelGpsDataSegement() {
        TermianlDynamicManage service = new TermianlDynamicManageImpl();
        String [] arr = null;
        service.delGpsDataSegementThisDay();
    }
}
