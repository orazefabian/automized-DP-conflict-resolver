package runtime;

import conflict.Object_D;

public class Object_C implements CInterface {
    private Object_D d = new Object_D();

    public Object_C() {
    }

    public void foo(int x) {
        this.d.bar(x);
    }
}
