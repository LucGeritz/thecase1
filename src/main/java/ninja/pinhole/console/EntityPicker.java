package ninja.pinhole.console;

import ninja.pinhole.model.Dao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An EntityPicker is a screen where rows of an entity become menu options.
 */
public class EntityPicker<T extends Pickable> extends Screen implements Launchable {

    private final String optionExit = "x";

    private Dao dao;
    private T pickedRow;
    private boolean needsAdmin;
    private boolean needsLogin;
    private RecordFilter<T> recordFilter;

    public EntityPicker(Container container,
                        String title,
                        UserIO userIO,
                        Dao dao,
                        boolean needsLogin,
                        boolean needsAdmin
    ) {
        super(container, title, userIO);
        this.dao = dao;
        this.needsAdmin = needsAdmin;
        this.needsLogin = needsLogin;
        this.recordFilter = (T rec) -> true;
    }

    @Override
    public void show() {
        this.options = getOptions();

        // Entity picker menu always returns to caller after pick
        var picked = getOption();

        // If choice was not exit then store the entity picked for retrieval by caller
        this.pickedRow = picked.getId() == optionExit ? null : ((EntityOption<T>) picked).getEntity();

    }

    public void setFilter(RecordFilter<T> filter) {
        this.recordFilter = filter;
    }

    public T getPicked() {
        return this.pickedRow;
    }

    public boolean hasPicked() {
        return pickedRow != null;
    }

    @Override
    public boolean needsAdminRights() {
        return needsAdmin;
    }

    @Override
    public boolean needsLogIn() {
        return needsLogin;
    }

    @Override
    public void launch() {
        show();
    }

    /**
     * All records that pass through recordfilter are added to the menu
     * They can be selected by their id.
     */
    private Map<String, Option> getOptions() {

        List<T> rows = dao.findAll();

        Map<String, Option> options = new TreeMap<>();
        rows.forEach(row -> {
            if (this.recordFilter.filter(row)) {
                String key = row.getIdAsString();
                EntityOption<T> option = new EntityOption<>(key, row.getRowAsString(), row);
                options.put(row.getIdAsString(), option);
            }
        });

        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

}
