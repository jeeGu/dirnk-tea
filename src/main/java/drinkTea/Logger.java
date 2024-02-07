package drinkTea;

import java.time.LocalDateTime;

public class Logger {
    public static void info(String message) {
        LocalDateTime currentTime = LocalDateTime.now();
        String currentName = Thread.currentThread().getName();
        System.out.println("[INFO][" + currentTime + "] " + "[" + currentName + "]" + message);
    }

    public static void main(String[] args) {
        info("This is an info log message.");
    }
}
