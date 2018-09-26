
package myframelib.zkzhou.com.common.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {
    
    /**
     * 
     * @param cmd   命令存放参数
     * @param workdirectory
     * @return
     * @throws IOException
     */
    public synchronized String run(String[] cmd, String workdirectory) throws IOException {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            if (workdirectory != null)
                builder.directory(new File(workdirectory));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] buffer = new byte[1024];
            int ret = in.read(buffer);
            while (ret != -1) {
                String temp = new String(buffer, 0, ret);
                result = result + temp;
                ret = in.read(buffer);
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
