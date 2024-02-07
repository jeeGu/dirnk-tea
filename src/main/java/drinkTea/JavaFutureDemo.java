package drinkTea;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class JavaFutureDemo {
    public static final int SLEEP_CAP = 500;

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                Logger.info("洗好水壶");
                Logger.info("灌上凉水");
                Logger.info("放在火上");
                // 线程睡眠一段时间，代表烧水中。。。
                Thread.sleep(SLEEP_CAP);
                Logger.info("水开了");
            } catch (InterruptedException e) {
                Logger.info("烧水发生异常被中断 ");
                return false;
            }
            Logger.info("烧水运行结束。");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                Logger.info("洗茶壶");
                Logger.info("洗茶杯");
                Logger.info("拿茶叶");
                // 线程睡眠一段时间，代表清洗中。。。
                Thread.sleep(SLEEP_CAP);
                Logger.info("洗完了");
            } catch (InterruptedException e) {
                Logger.info("清洗发生异常被中断 ");
                return false;
            }
            Logger.info("清洗运行结束。");
            return true;
        }
    }

    public static void drinkTea(boolean waterOk, boolean cupOk) {
        if (waterOk && cupOk) {
            Logger.info("泡茶喝");

        } else if (!waterOk) {
            Logger.info("烧水失败了，没有茶喝了");
        } else if (!cupOk) {
            Logger.info("杯子洗不了，没有茶喝了");
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("主线程");
        Callable<Boolean> hJob = new HotWaterJob();
        FutureTask<Boolean> hTask = new FutureTask<>(hJob);
        Thread hThread = new Thread(hTask, "** 烧水-Thread");
        Callable<Boolean> wJob = new WashJob();
        FutureTask<Boolean> wTask = new FutureTask<>(wJob);
        Thread wThread = new Thread(wTask, "$$ 清洗-Thread");
        hThread.start();
        wThread.start();
        // ... 等待烧水和清洗时可以干点其他的事情

        try {
            Boolean waterOk = hTask.get();
            Boolean cupOk = wTask.get();
            drinkTea(waterOk, cupOk);
        } catch (InterruptedException e) {
            Logger.info(getCurThreadName() + "发生异常被中断了");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Logger.info(getCurThreadName()+"运行结束");
    }
}
