package ninja.pinhole.console;

import ninja.pinhole.services.Launcher;

@FunctionalInterface
public interface LauncherInstantiator {
   Launcher create();
}
