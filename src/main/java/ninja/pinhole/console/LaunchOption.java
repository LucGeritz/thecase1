package ninja.pinhole.console;

import ninja.pinhole.services.Launcher;

/**
 * A launch option is an option that launches a Launchable
 */
public class LaunchOption extends Option{

    private LauncherInstantiator li;
    private boolean launchResult;
    private String launchErrMsg;


    public LaunchOption(String id, String name,LauncherInstantiator li) {
        super(id, name);
        this.li  = li;
    }

    @Override
    protected void afterPick(){
        Launcher launcher = li.create().launch();
        launchErrMsg = launcher.getErrorMsg();
        launchResult = launchErrMsg == "";
    }

    private boolean getLaunchResult(){
        return launchResult;
    }

    private String getLaunchErrorMsg(){
        return launchErrMsg;
    }
}
