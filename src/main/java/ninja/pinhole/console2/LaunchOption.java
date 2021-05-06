package ninja.pinhole.console2;

public class LaunchOption extends MenuOption {

    private Launcher launcher;

    public LaunchOption(String key, String text, Launcher launcher) {
        super(key, text);
        this.launcher = launcher;
    }

    public ActionResult doAction() {
        launcher.launch();
        return new ActionResult(
            launcher.getLaunchMessage(),
            launcher.wasLaunchedOk() ? MessageType.NONE : MessageType.ERROR
        );
    }


}
