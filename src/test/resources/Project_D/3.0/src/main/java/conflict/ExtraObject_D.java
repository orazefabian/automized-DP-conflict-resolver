package conflict;

/*********************************
 Created by Fabian Oraze on 29.12.20
 *********************************/

public class ExtraObject_D extends Object_D {

    public ExtraObject_D(){

    }

    @Override
    public void bar(int x) {
        super.bar(x);
    }

    public void bar(int x, int y) {
        System.out.println(x + y);
    }
}
