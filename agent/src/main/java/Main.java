import agentmain.biz.UserService;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        for (;;){
            System.out.println("main....1");
            UserService userService = new UserService();
            System.out.println("main....2");
            System.out.println(userService.getUserName());


            Field[] field  = UserService.class.getFields();
            System.out.println(field.length);

            try {
                Thread.sleep(3000);
                throw new RuntimeException("test......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
