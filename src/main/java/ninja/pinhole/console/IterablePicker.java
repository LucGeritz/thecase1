package ninja.pinhole.console;

import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import java.util.HashMap;
import java.util.Map;

public class IterablePicker extends Screen implements Launchable {

    private Option pickedOption;

    public IterablePicker(Iterable rawOptions, Container container, String title, UserIO userIO) {
        super(container, title, userIO);
        this.options = getOptions(rawOptions);
    }

    @Override
    public void show() {

        var picked = getOption();
        // If choice was not exit then store the entity picked for retrieval by caller
        pickedOption = picked;

    }

    private Map<String, Option> getOptions(Iterable rawOptions) {

        Map<String, Option> options = new HashMap<>();

        Integer i = 0;
        for (Object rawOption : rawOptions) {
            options.put(i.toString(), new Option(i.toString(), rawOption.toString()));
            i++;
        }

        options.put("x", new Option("x", "Exit"));

        return options;
    }

    public Option getPickedOption() {
        return pickedOption;
    }

    public boolean hasPicked() {
        return pickedOption != null;
    }

    @Override
    public boolean needsAdminRights() {
        // todo: fill in!
        return false;
    }

    @Override
    public boolean needsLogIn() {
        // todo: fill in!
        return false;
    }

    @Override
    public void launch() {
        show();
    }
}
