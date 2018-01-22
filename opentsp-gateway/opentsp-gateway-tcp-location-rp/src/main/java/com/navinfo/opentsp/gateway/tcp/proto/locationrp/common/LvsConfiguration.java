package com.navinfo.opentsp.gateway.tcp.proto.locationrp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class LvsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LvsConfiguration.class);

    @Value("${opentsp.node.config.node.code}")
    private String nodeCode;
    @Value("${opentsp.node.config.node.ip}")
    private String nodeIp;
    @Value("${opentsp.node.config.node.port}")
    private int nodePort;
    @Value("${opentsp.server.mm.master.ip}")
    private String mmMasterIp;
    @Value("${opentsp.server.mm.master.port}")
    private int mmMasterPort;
    @Value("${opentsp.server.rp.master.ip}")
    private String rpMasterIp;
    @Value("${opentsp.server.rp.master.port}")
    private int rpMasterPort;
    @Value("${opentsp.server.rpws.master.ip}")
    private String rpwsMasterIp;
    @Value("${opentsp.server.rpws.master.port}")
    private int rpwsMasterPort;

    public String getRpwsMasterIp() {
        return rpwsMasterIp;
    }

    public void setRpwsMasterIp(String rpwsMasterIp) {
        this.rpwsMasterIp = rpwsMasterIp;
    }

    public int getRpwsMasterPort() {
        return rpwsMasterPort;
    }

    public void setRpwsMasterPort(int rpwsMasterPort) {
        this.rpwsMasterPort = rpwsMasterPort;
    }

    public String getRpMasterIp() {
        return rpMasterIp;
    }

    public void setRpMasterIp(String rpMasterIp) {
        this.rpMasterIp = rpMasterIp;
    }

    public int getRpMasterPort() {
        return rpMasterPort;
    }

    public void setRpMasterPort(int rpMasterPort) {
        this.rpMasterPort = rpMasterPort;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }


    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getMmMasterIp() {
        return mmMasterIp;
    }

    public void setMmMasterIp(String mmMasterIp) {
        this.mmMasterIp = mmMasterIp;
    }

    public int getMmMasterPort() {
        return mmMasterPort;
    }

    public void setMmMasterPort(int mmMasterPort) {
        this.mmMasterPort = mmMasterPort;
    }

    @PostConstruct
    public void initConfig() {
        //config.put();
    }

}
