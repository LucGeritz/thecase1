package ninja.pinhole.screens;

import ninja.pinhole.console.EntityPicker;
import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserInterface;
import ninja.pinhole.model.Product;
import ninja.pinhole.model.ProductDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.TreeMap;

public class ProductScreen extends Screen implements Launchable {

    public final String optionAdd = "1";
    public final String optionEdit = "2";
    public final String optionDelete = "3";
    public final String optionExit = "x";

    public ProductScreen(Container container, UserInterface userInterface) {
        super(container,"Artikelen", userInterface);
        this.options = getOptions();
    }

    @Override
    public void show() {

        boolean show = true;

        while (show) {
            Option picked = this.getOption();

            switch (picked.getId()) {
                case optionExit:
                    show = false;
                    break;
                case optionAdd:
                    generalMsg = "Nog niet geïmplementeerd";
                    break;
                case optionDelete:
                    generalMsg = "Nog niet geïmplementeerd";
                    break;
                case optionEdit:
                    editProduct();
                    break;
            }
        }
    }


    @Override
    public boolean needsAdminRights() {
        return Launchable.NEEDSNOADMIN;
    }

    @Override
    public boolean needsLogIn() {
        return Launchable.NEEDSLOGIN;
    }

    @Override
    public void launch() {
        show();
    }

    private Map<String, Option> getOptions() {
        Map<String, Option> options = new TreeMap<>();
        options.put(optionAdd, new Option(optionAdd, "Toevoegen"));
        options.put(optionEdit, new Option(optionEdit, "Wijzigen"));
        options.put(optionDelete, new Option(optionDelete, "Verwijderen"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    /**
     * Let user pick a product, if picked launch product edit screen
     */
    private void editProduct() {

        var ep = new EntityPicker<Product>(container,"Kies artikel",
                userInterface,
                new ProductDao(container.<EntityManager>get("em")),
                Launchable.NEEDSLOGIN,
                Launchable.NEEDSADMIN);
        if (launch(ep) && ep.hasPicked()) {
            launch(new ProductEditScreen(container, userInterface, ep.getPicked()));
        }
    }

}

