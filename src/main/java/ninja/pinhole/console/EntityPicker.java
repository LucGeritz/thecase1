package ninja.pinhole.console;


import ninja.pinhole.model.Dao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
@TODO create builder, constructor takes 5 parameters!
 */
public class EntityPicker<T extends Pickable> extends Screen implements Launchable {

    private Dao dao;
    private T pickedRow;
    private boolean needsAdmin;
    private boolean needsLogin;
    private RecordFilter<T> recordFilter;

    public EntityPicker(Container container, String title,
                        UserInterface userInterface,
                        Dao dao,
                        boolean needsLogin,
                        boolean needsAdmin
    ) {
        super(container, title, userInterface);
        this.dao = dao;
        this.needsAdmin = needsAdmin;
        this.needsLogin = needsLogin;
        this.recordFilter = (T rec) -> true;
        // this.options = getOptions();
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
    public void show() {
        // moved out of constructor to allow caller to set filter;
        this.options = getOptions();
        // Entity picker menu always returns to caller after pick
        var picked = getOption();
        // If choice was not exit then store the entity picked for retrieval by caller
        this.pickedRow = picked.getId() == "x" ? null : ((EntityOption<T>) picked).getEntity();

    }

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

        options.put("x", new Option("x", "Exit"));

        return options;
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
}
