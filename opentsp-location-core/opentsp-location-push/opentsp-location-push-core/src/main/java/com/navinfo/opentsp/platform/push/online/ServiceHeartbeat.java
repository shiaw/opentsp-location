//package com.navinfo.opentsp.platform.push.online;
//
//import com.netflix.discovery.DiscoveryClient;
//import com.netflix.discovery.shared.Application;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//
///**
// * @author wanliang
// * @version 1.0
// * @date 2016/8/11
// * @modify
// * @copyright opentsp
// */
//@Component
//public class ServiceHeartbeat {
//
//    @Autowired
//    private DiscoveryClient eurekaDiscoveryClient;
//
//    @PostConstruct
//    public void getService(){
//      List<Application> services= eurekaDiscoveryClient.getApplications().getRegisteredApplications();
//      for (Application application:services){
//          System.out.println(application.getName());
//      }
//    }
//}
