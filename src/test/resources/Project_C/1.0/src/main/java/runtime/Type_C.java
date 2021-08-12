package runtime;

import conflict.Object_D;

/*********************************
 Created by Fabian Oraze on 18.03.21
 *********************************/

public class Type_C {
    private Object_D d = new Object_D();

    public Type_C() {
    }

    public void bar(int x) {
        this.kek(2 * x);
    }

    public void kek(int x) {
        Object_C c = new Object_C();
        c.speck(this.d, x);
    }
}