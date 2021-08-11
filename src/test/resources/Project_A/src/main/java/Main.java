import runtime.CInterface;
import runtime.Object_B;
import runtime.Object_C;

import java.io.File;
import java.net.URISyntaxException;

/*********************************
 Created by Fabian Oraze on 26.11.20
 *********************************/

public class Main {

    public static void main(String[] args) {

        Object_B b = new Object_B();
        CInterface c = new Object_C();

        int x = 2;
        b.foo(x);
        c.foo(x);
        b.pep(x * 2); //only when version 2.0 of Project_B is used

    }

}
