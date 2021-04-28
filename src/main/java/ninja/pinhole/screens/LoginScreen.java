package ninja.pinhole.screens;

import ninja.pinhole.console.InputOption;
import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserIO;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;
import ninja.pinhole.services.LoginService;

import java.util.Map;
import java.util.TreeMap;

public class LoginScreen extends Screen implements Launchable {

    private final String optionLog = "L";
    private final String optionName = "1";
    private final String optionPw = "2";
    private final String optionExit = "x";

    private LoginService lis;

    public LoginScreen(Container container, UserIO userIO) {
        super(container,"Log in / Log uit", userIO);
        this.options = getOptions();
    }

    @Override
    public void show() {

        boolean show = true;

        while(show){

            // show login or logout?
            setLogOption();
            Option picked = getOption();

            switch (picked.getId()) {
                case optionExit:
                    show = false;
                    break;
                case optionLog:
                    if(getLoginService().isLoggedIn()){
                        lis.logout();
                        options.get(optionPw).setValue("");
                        options.get(optionName).setValue("");
                    }
                    else{
                        this.login();
                    }
                    break;
            }
        }

    }

    @Override
    public boolean needsAdminRights() {
        return false;
    }

    @Override
    public boolean needsLogIn() {
        return false;
    }

    @Override
    public void launch() {
        this.show();
    }

    private void login(){
        String user = this.options.get(optionName).getValue();
        String pw = this.options.get(optionPw).getValue();

        if(!getLoginService().login(user, pw)){
            generalMsg = "Inloggen niet gelukt!";
        }

    }

    /**
     * Return menu option based on being logged in/out
     */
    private void setLogOption(){
        options.put(optionLog, getLoginService().isLoggedIn() ? new Option(optionLog, "Log out"):new Option(optionLog, "Log in"));
    }

    private LoginService getLoginService(){
        if(lis  == null){
            lis = container.get("lis");
        }
        return lis;
    }

    private Map<String, Option> getOptions() {

        Map<String, Option> options = new TreeMap<>();
        options.put(optionName, new InputOption(optionName, "Naam", userIO));
        options.put(optionPw, new InputOption(optionPw, "Wachtwoord", userIO).setSecret());
        options.put(optionLog, new Option(optionLog, "Log in"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

}

