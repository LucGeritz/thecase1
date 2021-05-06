package ninja.pinhole.console2;

import ninja.pinhole.services.LoginService;

public class LogoutOption extends MenuOption{

    private LoginService lis;

    public LogoutOption(String key, String text, LoginService lis) {
        super(key, text, "");
        this.lis = lis;
    }

    @Override
    public ActionResult doAction() {
        lis.logout();
        boolean suc6 = !lis.isLoggedIn();
        return new ActionResult(
            "Uitloggen " + (suc6 ? "": "NIET ")+ "gelukt",
            suc6 ? MessageType.INFO : MessageType.ERROR
        );
    }
}
