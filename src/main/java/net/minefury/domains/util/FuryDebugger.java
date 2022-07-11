package net.minefury.domains.util;


import com.modify.fundamentum.text.PlugLogger;
import lombok.Getter;
import lombok.Setter;

public class FuryDebugger {

    @Getter @Setter private boolean debugMode;

    public FuryDebugger() {
        this.debugMode = false;
    }

    public void sendDebugInfo(String s) {
        if (isDebugMode()) {
            PlugLogger.logInfo(s);
        }
    }

    public void sendDebugError(String s) {
        if (isDebugMode()) {
            PlugLogger.logError(s);
        }
    }

    public void sendDebugWarning(String s) {
        if (isDebugMode()) {
            PlugLogger.logWarning(s);
        }
    }

}
