package com.navinfo.opentsp.platform.da.core.common;

/**
 * @author: ChenJie
 * @date 2017/10/28
 */
public class ConcentratedData {
    /**
     * 终端id
     */
    private String terminalId;
    /**
     * redis zset排序分数，即时间戳(s)
     **/
    private Double score;
    /**
     * 数据
     * */
    private String data;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ConcentratedData{" +
                "terminalId='" + terminalId + '\'' +
                ", score=" + score +
                ", data='" + data + '\'' +
                '}';
    }
}
