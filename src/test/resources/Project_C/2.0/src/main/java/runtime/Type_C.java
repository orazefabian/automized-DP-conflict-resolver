package runtime;

import conflict.Object_D;

public class Type_C {
    private Object_D d = new Object_D();

    public Type_C() {
    }

    public void bar(int x) {
        this.d.bar(x);
    }
}
