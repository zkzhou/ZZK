package myframelib.zkzhou.com.common.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

    private static ThreadPoolUtil sPool = null;

    private ExecutorService mPoolService = null;
//    private int CPU_COUNT=Runtime.getRuntime().availableProcessors();
//    private int THREADS=CPU_COUNT*2+5;
    private ThreadPoolUtil() {
//        mPoolService = Executors.newFixedThreadPool(THREADS);
        mPoolService=Executors.newCachedThreadPool();
    }

    public  static ThreadPoolUtil getInstance() {
        if (sPool == null) {
            init();
        }
        return sPool;
    }
    private static synchronized void init(){
        sPool = new ThreadPoolUtil();
    }
    public void execute(Runnable task) {
        if (task != null && !mPoolService.isShutdown()) {
            mPoolService.execute(task);
        }
    }

    public void shutdown() {
        mPoolService.shutdown();
        sPool = null;
        mPoolService = null;
    }
}
