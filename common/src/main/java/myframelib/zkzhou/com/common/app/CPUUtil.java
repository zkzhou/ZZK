
package myframelib.zkzhou.com.common.app;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CPUUtil {

    private static final String TAG = "cpu";

    public static final int TYPE_ARMV5 = 1;

    public static final int TYPE_ARMV6 = 2;

    public static final int TYPE_ARMV7 = 3;

    public static final int TYPE_X86 = 4;

    public static final int TYPE_MIPS = 5;

    public static final int TYPE_UNKNOW = -1;

    /**
     * <ul>
     * <li>Processor : ARMv7 Processor rev 1 (v7l)</li>
     * <li>BogoMIPS : 681.57</li>
     * <li>Features : swp half thumb fastmult vfp edsp thumbee neon vfpv3</li>
     * <li>CPU implementer : 0x51</li>
     * <li>CPU architecture: 7</li>
     * <li>CPU variant : 0x1</li>
     * <li>CPU part : 0x00f</li>
     * <li>CPU revision : 1</li>
     * <li>Hardware : saga</li>
     * <li>Revision : 0080</li>
     * <li>Serial :0000000000000000</li>
     * </ul>
     * 
     * @return
     */
    private static String fetchCpuInfo() {
        String result = null;
        CMDExecute cmdexe = new CMDExecute();
        try {
            String[] args = {
                    "/system/bin/cat", "/proc/cpuinfo"
            };
            result = cmdexe.run(args, "/system/bin/");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static int getCPUType() {
        String abi = android.os.Build.CPU_ABI.toLowerCase().trim();
        Log.d(TAG, "CPU_ABI: " + abi);
        if (abi.equals("armeabi-v7a")) {
            return TYPE_ARMV7;
        } else if (abi.equals("x86")) {
            return TYPE_X86;
        } else if (abi.equals("mips")) {
            return TYPE_MIPS;
        } else {
            String info = fetchCpuInfo();
            Log.d(TAG, info);
            String[] args = info.split("\n");
            for (int i = 0; i < args.length; i++) {
                String name = args[i];
                if (name.contains(":")) {
                    String[] args1 = name.split(":");
                    if (args1[0].toLowerCase().contains("processor")) {
                        String result = args1[1].trim().toLowerCase();
                        if (result.contains("v7")) {
                            return TYPE_ARMV7;
                        } else if (result.contains("v6")) {
                            return TYPE_ARMV6;
                        } else if (result.contains("v5")) {
                            return TYPE_ARMV5;
                        } else if (result.contains("x86")) {
                            return TYPE_X86;
                        } else if (result.contains("mips")) {
                            return TYPE_MIPS;
                        }
                    } else if (args1[0].toLowerCase().contains("features")) {
                        String result = args1[1].trim().toLowerCase();
                        if (result.contains("neon")) {
                            return TYPE_ARMV7;
                        }
                    } else if (args1[0].toLowerCase().contains("architecture")) {
                        String result = args1[1].trim().toLowerCase();
                        if (result.contains("7")) {
                            return TYPE_ARMV7;
                        } else if (result.contains("6")) {
                            return TYPE_ARMV6;
                        } else if (result.contains("5")) {
                            return TYPE_ARMV5;
                        }
                    }
                }
            }
            return TYPE_UNKNOW;
        }
    }

    /**
     * 实时获取CPU当前频率（单位KHZ）
     * 
     * @return
     */
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取CPU名字
     * 
     * @return
     */
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
