package ninja.pinhole.console2;

import ninja.pinhole.services.Launchable;

public interface Launcher {
    Launcher launch();
    String getLaunchMessage();
    int getLaunchResult();

    default boolean wasLaunchedOk(){
        return getLaunchResult() == 0;
    }
}
