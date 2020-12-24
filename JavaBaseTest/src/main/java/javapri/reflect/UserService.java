package javapri.reflect;


import javapri.reflect.annotation.Gray;

public class UserService {

    @Gray()
    public String getPassword(String userName){
        return "123456";
    }


    public String getPasswordGrade(){
        return "987654";
    }



}
