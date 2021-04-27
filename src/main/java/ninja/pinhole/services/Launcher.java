package ninja.pinhole.services;

/**
 * Facilitates launching a program
 * 'program' has to be class implementing Launchable
 * Launcher checks if user is logged in if needed
 * Launcher checks if user is admin if needed
 * Depends on LoginService
 */
public class Launcher {

    private String errorMsg;
    private LoginService lis;

    public Launcher(LoginService lis){
        this.lis = lis;
    }

    /**
     * Return error message of last launch.
     *
     * @return empty if no error
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean launchedOk() {
        return errorMsg.equals("");
    }

    /**
     * @return Launcher to make it chainable.
     */
    public Launcher launch(Launchable program) {
        errorMsg = "";

        if (!loggedInCheck(program, lis)) {
            this.errorMsg = "U dient ingelogd te zijn";
            return this;
        }

        if (!adminCheck(program, lis)) {
            this.errorMsg = "U dient beheerder te zijn";
            return this;
        }

        program.launch();

        return this;
    }

    /**
     * @return true if login is needed and caller is logged in, or no login needed
     */
    private boolean loggedInCheck(Launchable program, LoginService lis) {
        return program.needsLogIn() ? lis.isLoggedIn() : true;
    }

    /**
     * @return true if admin is needed and caller is admin, or no admin needed
     */
    private boolean adminCheck(Launchable program, LoginService lis) {
        return program.needsAdminRights() ? lis.isCurrentUserAdmin() : true;
    }


}
