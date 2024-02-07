package drinkTea;

import java.util.concurrent.CompletableFuture;

public class DrinkTea {

    private static final int SLEEP_CAP = 3000;

    public static void main(String[] args) {
        final long l = System.currentTimeMillis();

        var hotJob = CompletableFuture.supplyAsync(() -> {
            Logger.info("洗好水壶了");
            Logger.info("烧开水");
            sleepSeconds(SLEEP_CAP);
            return true;
        });

        var washJob = CompletableFuture.supplyAsync(() -> {
            Logger.info("洗茶杯");
            sleepSeconds(SLEEP_CAP);
            Logger.info("茶杯洗完了");
            return true;
        });

        var drinkJob = hotJob.thenCombine(washJob,
                (hodOk, washOk) -> {
                    if (hodOk && washOk) {
                        Logger.info("泡茶喝， 茶喝完");
                        return "茶喝完了";
                    }
                    return "没有喝到茶";
                });

        Logger.info(drinkJob.join());
        Logger.info("time = " + (System.currentTimeMillis() -l));

    }

    private static void sleepSeconds(int sleepCap) {
        try {
            Thread.sleep(sleepCap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
