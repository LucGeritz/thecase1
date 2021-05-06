package ninja.pinhole.console2;

public class PlainLauncher implements Launcher {

    private Launchable program;
    private String launchMessage;
    private int launchResult;

    public PlainLauncher(Launchable program) {
        this.program = program;
    }

    @Override
    public Launcher launch() {
        launchMessage = "";
        launchResult = 0;
        program.launch();
        return this;
    }

    @Override
    public String getLaunchMessage() {
        return launchMessage;
    }

    @Override
    public int getLaunchResult() {
        return launchResult;
    }


}
