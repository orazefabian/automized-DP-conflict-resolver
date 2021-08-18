package runtime;
import conflict.ExtraObject_D;
import conflict.Object_D;


public class Parent_B {
    private ExtraObject_D extra = new ExtraObject_D();

    public Parent_B() {
    }

    public void pep(int x) {
        this.extra.bar(x, x);
    }
}
