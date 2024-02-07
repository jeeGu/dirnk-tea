package drinkTea;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuavaFutureDemo {
    public final static int SLEEP_CAP = 500;
    static class HotWaterJob implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            try {
                Logger.info("洗好茶壶");
                Logger.info("烧开水");
                Thread.sleep(SLEEP_CAP);
                Logger.info("水开了");
            } catch (InterruptedException e) {
                Logger.info("发生了异常中断");
                return false;
            }
            Logger.info("烧水工作，运行结束。 ");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            try {
                Logger.info("洗茶杯");
                Thread.sleep(SLEEP_CAP);
                Logger.info("洗完了");
            } catch (InterruptedException e) {
                Logger.info("清洗工作发生异常被中断");
                return false;
            }
            Logger.info("清洗工作运行结束。");
            return true;
        }
    }


    static class DrinkJob {
        boolean waterOk = false;
        boolean cupOk = false;


        public void drinkTea() {
            if (waterOk && cupOk) {
                Logger.info("泡茶喝，茶喝完了 ");
                this.waterOk = false;
            }
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("泡茶喝线程");
        // 新起一个线程，作为泡茶主线程
        DrinkJob drinkJob = new DrinkJob();

        // 烧水线程的业务逻辑
        Callable<Boolean> hotWaterJob = new HotWaterJob();

        // 清洗线程的业务逻辑
        Callable<Boolean> washJob = new WashJob();

        // 创建Java线程池
        ExecutorService jPool = Executors.newFixedThreadPool(10);

        // 包装Java 线程池，构造Guava线程池
        ListeningExecutorService gPool = MoreExecutors.listeningDecorator(jPool);

        // 烧水的回调钩子
        FutureCallback<Boolean> hotWaterHook = new FutureCallback<>() {
            @Override
            public void onSuccess(@Nullable Boolean r) {
                if (r) {
                    drinkJob.waterOk = true;
                    // 执行回调方法
                    drinkJob.drinkTea();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Logger.info("烧水失败了，没有茶喝了");
            }
        };

        // 启动烧水线程
        ListenableFuture<Boolean> hotFuture = gPool.submit(hotWaterJob);

        // 设置烧水任务的回调钩子
        Futures.addCallback(hotFuture, hotWaterHook, gPool);

        // 启动清洗线程
        ListenableFuture<Boolean> washFuture = gPool.submit(washJob);

        // 使用匿名实例，作为清洗之后的回调钩子
        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean r) {
                if (r) {
                    drinkJob.cupOk = true;
                    // 执行回调方法
                    drinkJob.drinkTea();
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                Logger.info("杯子洗不了， 没有茶喝了 ");
            }
        }, gPool);

        Logger.info("干点其他的事情。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Logger.info("执行完成了");
    }


}
