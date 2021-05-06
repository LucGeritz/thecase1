package ninja.pinhole.console2;

import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;

/**
 * A launcher which knows about authorization
 * It knows about it through the passed di-container.
 */
public class AuthLauncher implements Launcher {

    private final int notLoggedIn = 1;
    private final int notAdmin = 2;
    private final Container container;
    private final Launchable program;
    private String launchMessage;
    private int launchResult;
    private boolean needsLogin;
    private boolean needsAdmin;

    public AuthLauncher(Container container, Launchable program) {
        this.program = program;
        this.container = container;
    }

    @Override
    public Launcher launch() {

        LoginService lis = container.get("lis");

        launchMessage = "";
        launchResult = 0;

        if (!loggedInCheck(lis)) {
            launchMessage = "U dient ingelogd te zijn";
            launchResult = notLoggedIn;
            return this;
        }

        if (!adminCheck(lis)) {
            launchMessage = "U dient beheerder te zijn";
            launchResult = notAdmin;
            return this;
        }

        program.launch();

        return this;

    }

    @Override
    public String getLaunchMessage() {
        return launchMessage;
    }

    @Override
    /**
     * Returns 1 if not logged in but log in required
     * Returns 2 if logged in but not as admin if admin required
     * Returns 0 if authorization ok
     */
    public int getLaunchResult() {
        return launchResult;
    }

    public AuthLauncher requiresAdmin() {
        needsAdmin = true;
        needsLogin = true;
        return this;
    }

    public AuthLauncher requiresLogin() {
        needsLogin = true;
        return this;
    }

    /**
     * @return true if login is needed and caller is logged in, or no login needed
     */
    private boolean loggedInCheck(LoginService lis) {
        return needsLogin ? lis.isLoggedIn() : true;
    }

    /**
     * @return true if admin is needed and caller is admin, or no admin needed
     */
    private boolean adminCheck(LoginService lis) {
        return needsAdmin ? lis.isCurrentUserAdmin() : true;
    }

}
