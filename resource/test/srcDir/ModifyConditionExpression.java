
public class ModifyConditionExpression {
	final double a = 5;
    static final Context getApplicationContextIfAvailable(Context context) {
        final Context ac = context.getApplicationContext();
        return ac != null ? ac : ActivityThread.currentApplication().getApplicationContext();
    }
}