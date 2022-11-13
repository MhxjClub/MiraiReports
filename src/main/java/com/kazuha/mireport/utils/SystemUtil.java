package com.kazuha.mireport.utils;


import java.io.*;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.StringTokenizer;


import com.kazuha.mireport.main;
import com.sun.management.OperatingSystemMXBean;
import net.md_5.bungee.api.ProxyServer;



public class SystemUtil {
    private static final int CPUTIME = 500;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    public static float cpuStats = (float) 0.0;

    /**
     * 获得Linux cpu使用率
     * @return float efficiency
     * @throws IOException
     * @throws InterruptedException
     */
    public static float getCpuInfo() {
        ProxyServer.getInstance().getScheduler().runAsync(main.instance,()->{
            try {
                File file = new File("/proc/stat");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
                StringTokenizer token = new StringTokenizer(br.readLine());
                token.nextToken();
                long user1 = Long.parseLong(token.nextToken() + "");
                long nice1 = Long.parseLong(token.nextToken() + "");
                long sys1 = Long.parseLong(token.nextToken() + "");
                long idle1 = Long.parseLong(token.nextToken() + "");


                Thread.sleep(1000);


                br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file)));
                token = new StringTokenizer(br.readLine());
                token.nextToken();
                long user2 = Long.parseLong(token.nextToken());
                long nice2 = Long.parseLong(token.nextToken());
                long sys2 = Long.parseLong(token.nextToken());
                long idle2 = Long.parseLong(token.nextToken());


                cpuStats = (float) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1)) * 100
                        / (float) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1
                        + sys1 + idle1));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        return cpuStats;
    }

    public static BigDecimal getLoad() {
        try {
            Runtime r = Runtime.getRuntime();
            BigDecimal cpuPerformance = new BigDecimal(0.7).multiply(new BigDecimal(r.availableProcessors()));
            Process pro = r.exec("uptime");
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String topLoad = br.readLine();
            BigDecimal load = new BigDecimal(topLoad.substring(topLoad.lastIndexOf(" ")+1));
            BigDecimal pload = new BigDecimal(100).multiply(load).divide(cpuPerformance, 0, BigDecimal.ROUND_HALF_UP);
            br.close();
            pro.destroy();
            return pload;
        } catch (Exception e) {
            return new BigDecimal(-1);
        }
    }

    public static float getMemory() {
        float memUsage = 0.0f;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/meminfo";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count = 0;
            long totalMem = 0, freeMem = 0,spmem =0;
            while((line=in.readLine()) != null){
                String[] memInfo = line.split("\\s+");
                if(memInfo[0].startsWith("MemTotal")){
                    totalMem = Long.parseLong(memInfo[1]);
                }
                if(memInfo[0].startsWith("MemAvailable")){
                    freeMem = Long.parseLong(memInfo[1]);
                }
                if(memInfo[0].startsWith("MemFree")){
                    spmem = Long.parseLong(memInfo[1]);
                }
                memUsage = (1- ((float)freeMem+(float) spmem)/(float)totalMem)*100f;

                if(++count == 3){
                    break;
                }
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
        }
        return memUsage;
    }

}