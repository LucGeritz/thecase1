package ninja.pinhole.screens;

import ninja.pinhole.services.Launchable;

public class TestLaunchable implements Launchable {

    @Override
    public boolean needsAdminRights() {
        return false;
    }

    @Override
    public boolean needsLogIn() {
        return true;
    }

    @Override
    public void launch() {
        System.out.println("Working!");
    }
}
