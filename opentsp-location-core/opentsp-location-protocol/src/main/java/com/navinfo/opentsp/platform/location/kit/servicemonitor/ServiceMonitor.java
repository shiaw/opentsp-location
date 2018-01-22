package  com.navinfo.opentsp.platform.location.kit.servicemonitor;
import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.location.kit.servicemonitor.mode.*;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;


public class ServiceMonitor {
    private static SigarProxy sigar;
    private static org.hyperic.sigar.CpuInfo[] cpuInfos;
    private static final String osName = System.getProperty("os.name");
    private static Runtime rt;
    private final static ServiceMonitor instance = new ServiceMonitor();

    private ServiceMonitor() {
        sigar = new Sigar();
        try {
            cpuInfos = sigar.getCpuInfoList();
        } catch (SigarException se) {
        }
        rt = Runtime.getRuntime();
    }
    public static final ServiceMonitor getInstance(){
        return instance;
    }
    /**
     * 获取内存信息
     * @return
     */
    public Memory getMemory() {
        Mem mem;
        Memory memory = new Memory();
        memory.setJvmTotalMemory((int)(rt.totalMemory()>> 20));
        memory.setJvmFreeMemory((int)(rt.freeMemory()>> 20));
        memory.setJvmMaxMemory((int)(rt.maxMemory()>> 20));
        memory.setJvmUsedMemory(memory.getJvmTotalMemory() - memory.getJvmFreeMemory());
        try {
            mem = sigar.getMem();
            memory.setPhysicalTotalMemory((int)(mem.getTotal()>>20));
            memory.setPhysicalFreePhysicalMemory((int)(mem.getFree()>>20));
            memory.setPhysicalUsedMemory((int)(mem.getUsed()>>20));
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return memory;
    }
    /***
     * 获取cpu状态
     * @return
     */
    public CpuInfo getCpu(){
        CpuInfo cpus = new CpuInfo();
        try {
            CpuPerc[] cpuPercs = sigar.getCpuPercList();
            CpuPerc cpuPerc = sigar.getCpuPerc();
            if(cpuInfos.length == 0 || cpuPercs.length == 0){
                return cpus;
            }
            //Cpu静态信息
            org.hyperic.sigar.CpuInfo _cpus = cpuInfos[0];
            cpus.setVendor(_cpus.getVendor());
            cpus.setModel(_cpus.getModel());
            cpus.setHz(MonitorUtil.getCpuHz(_cpus.getMhz()));
            cpus.setTotalCores(_cpus.getTotalCores());
            cpus.setTotalSockets(_cpus.getTotalSockets());
            cpus.setCoresPerSocket(_cpus.getCoresPerSocket());
            //Cpu动态信息汇总
            Cpu totalCpu = new Cpu();
            totalCpu.setUserPerc(cpuPerc.getUser());
            totalCpu.setSysPerc(cpuPerc.getSys());
            totalCpu.setNicePerc(cpuPerc.getNice());
            totalCpu.setIdlePerc(cpuPerc.getIdle());
            totalCpu.setWaitPerc(cpuPerc.getWait());
            totalCpu.setIrqPerc(cpuPerc.getIrq());
            if(osName.equals("Linux")){
                totalCpu.setSoftIrqPerc(cpuPerc.getSoftIrq());
                totalCpu.setStealPerc(cpuPerc.getStolen());
            }
            totalCpu.setCpuRatio(cpuPerc.getCombined());
            cpus.setTotalCpu(totalCpu);
            //收集各个Cpu信息
            List<Cpu> cpuList = new ArrayList<Cpu>();
            for (CpuPerc cpc : cpuPercs) {
                Cpu cpu = new Cpu();
                cpu.setUserPerc(cpc.getUser());
                cpu.setSysPerc(cpc.getSys());
                cpu.setNicePerc(cpc.getNice());
                cpu.setIdlePerc(cpc.getIdle());
                cpu.setWaitPerc(cpc.getWait());
                cpu.setIrqPerc(cpc.getIrq());
                if(osName.equals("Linux")){
                    cpu.setSoftIrqPerc(cpc.getSoftIrq());
                    cpu.setStealPerc(cpc.getStolen());
                }
                cpu.setCpuRatio(cpc.getCombined());
                cpuList.add(cpu);
            }
            cpus.setCpuList(cpuList);
        } catch (SigarException e) {
        }
        return cpus;
    }

    public ProcessInfo getProcess(){
        ProcessInfo processInfo = new ProcessInfo();
        return processInfo;
    }

    public static void main(String[] args) throws Exception {
        testCpu();
    }
    private static void testCpu(){
        while(true){
            System.err.println(ServiceMonitor.getInstance().getCpu());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static void testMemory(){
        System.err.println(ServiceMonitor.getInstance().getMemory());
        new Thread(new Runnable() {

            @Override
            public void run() {
                StringBuffer buffer = new StringBuffer();
                while(true){
                    buffer.append("Memory [jvmTotalMemory=61M, jvmFreeMemory=60M, jvmMaxMemory=907M, totalPhysicalMemorySize=3.98G, freePhysicalMemorySize=1.23G, usedPhysicalMemory=2.75G]");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        System.err.println(ServiceMonitor.getInstance().getMemory());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
