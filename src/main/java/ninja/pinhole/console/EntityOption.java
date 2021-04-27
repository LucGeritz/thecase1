package ninja.pinhole.console;

/**
 * An EntityOption represents an Option which is coupled to an entity
 */
public class EntityOption<T> extends Option{

    private T entity;


    public EntityOption(String id, String name, T entity)
    {
        super(id, name);
        this.entity = entity;
    }

    public T getEntity(){
        return this.entity;
    }

}
