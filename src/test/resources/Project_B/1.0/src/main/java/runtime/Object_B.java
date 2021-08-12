package runtime;

import conflict.Object_D;

public class Object_B {
    private Object_D d = new Object_D();

    public Object_B() {
    }

    public void foo(int x) {
        this.d.bar(x, x);
        this.d.kek(x);
    }

    public void test() {
        System.out.println(this.d.kek(2));
    }
}
