package javafx3D.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx3D.config.AppConfig;

/**
 * Centralizovaná třída pro logování v aplikaci.
 * Poskytuje strukturované logování s různými úrovněmi.
 */
public class Logger {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public enum LogLevel {
        DEBUG("[DEBUG]"),
        INFO("[INFO]"),
        WARN("[WARN]"),
        ERROR("[ERROR]");
        
        private final String prefix;
        
        LogLevel(String prefix) {
            this.prefix = prefix;
        }
        
        public String getPrefix() {
            return prefix;
        }
    }
    
    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    public static void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }
    
    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    public static void error(String message, Exception ex) {
        log(LogLevel.ERROR, message + " - " + ex.getMessage());
        if (AppConfig.DEBUG_MODE) {
            ex.printStackTrace();
        }
    }
    
    private static void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("%s %s: %s", timestamp, level.getPrefix(), message);
        
        if (level == LogLevel.ERROR) {
            System.err.println(logMessage);
        } else {
            System.out.println(logMessage);
        }
    }
    
}
