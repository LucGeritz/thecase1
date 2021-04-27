package ninja.pinhole.services;

/**
 * Make a class launchable by Launcher
 */
public interface Launchable {

    boolean NEEDSADMIN = true;
    boolean NEEDSLOGIN = true;
    boolean NEEDSNOADMIN = false;
    boolean NEEDSNOLOGIN = false;
    /**
     * Does user need to be admin to launch the program?
     *   If needsLogIn is false then needsAdminRight should return false as well.
     *      if not it still works fine, a user-not-loggied in is considered a
     *      user without admin rights.
     */
    boolean needsAdminRights();

    /**
     * Does user need to be logged in to launch the program?
     */
    boolean needsLogIn();

    void launch();
}
