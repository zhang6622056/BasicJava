package lambdas.basic.j8optional;

import java.util.Optional;

public class OptionalTest {

    public static String username = "";


    public static void main(String[] args) {
        String a = null;
        Optional<String> aoption = Optional.ofNullable(a);


        aoption.ifPresent(item -> username = item);
        System.out.println(username);
    }



}
