package ninja.pinhole.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Container containing ContainerAssets
 * ContainerAssets are system wide Objects
 * Is it a simple DiC? Or an advanced Registry..
 */
public class Container {

    private Map<String, ContainerAsset> assets = new HashMap<>();

    public <T> void set(String key, Instantiator<T> i) {
        assets.put(key, new ContainerAsset<>(i));
    }

    public <T> T get(String key) {
        ContainerAsset<T> cont = assets.get(key);
        if (cont != null) {
            return cont.getAsset();
        }
        return null;
    }

    private class ContainerAsset<T> {

        private T asset;
        private Instantiator<T> i;

        ContainerAsset(Instantiator<T> i) {
            this.i = i;
        }

        T getAsset() {
            if (asset == null) {
                asset = i.create();
            }
            return asset;
        }
    }
}