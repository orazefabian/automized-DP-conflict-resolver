package runtime;

import conflict.Object_D;

/*********************************
 Created by Fabian Oraze on 26.11.20
 *********************************/

public class Object_C implements CInterface {
    private Type_C c = new Type_C();

    public Object_C() {
    }

    public void foo(int x) {
        this.c.bar(x);
    }

    public void speck(Object_D y, int x) {
        y.bar(x);
    }
}
