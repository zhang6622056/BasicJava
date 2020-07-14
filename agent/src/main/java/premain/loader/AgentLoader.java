package premain.loader;

public class AgentLoader extends ClassLoader{

    private static Integer b = 123;

    static{
        b = 45;
    }



}
