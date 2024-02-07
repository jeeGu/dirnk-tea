package drinkTea;


public class JoinDemo {
    public final static int SLEEP_CAP = 500;

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterThread extends Thread {
        public HotWaterThread() {
            super("** 烧水-Thread");
        }

        public void run() {
            try {
                Logger.info("洗好水壶");
                Logger.info("灌上凉水");
                Logger.info("放在火上");
                // 线程睡眠一段时间，代表烧水中。。。
                Thread.sleep(SLEEP_CAP);
            } catch (InterruptedException e) {
                Logger.info("烧水发生异常被中断 ");
            }
            Logger.info("烧水运行结束。");
        }
    }


    static class WashCupThread extends Thread {
        public WashCupThread() {
            super("$$ 清洗-Thread");
        }

        public void run() {
            try {
                Logger.info("洗茶壶");
                Logger.info("洗茶杯");
                Logger.info("拿茶叶");
                // 线程睡眠一段时间，代表清洗中。。。
                Thread.sleep(SLEEP_CAP);
                Logger.info("洗完了");
            } catch (InterruptedException e) {
                Logger.info("清洗发生异常被中断 ");
            }
            Logger.info("清洗运行结束。");
        }
    }


    public static void main(String[] args) {
        Thread hThread = new HotWaterThread();
        Thread wThread = new WashCupThread();
        hThread.start();
        wThread.start();
        // ....在等待烧水和清洗时。可以干点其他的事情
        try {
            // 合并烧水-线程
            hThread.join();
            wThread.join();
            Thread.currentThread().setName("主线程");
            Logger.info("泡茶喝");
        } catch (InterruptedException e) {
            Logger.info(getCurThreadName() + "发生了异常被中断");
        }
        Logger.info(getCurThreadName() + "执行结束");
    }

}
