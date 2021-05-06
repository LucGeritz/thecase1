package ninja.pinhole.console2;

public class ActionResult {
    public final String message;
    public final MessageType type;

    public ActionResult(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }

    public ActionResult(){
        this("",MessageType.NONE);
    }

}
