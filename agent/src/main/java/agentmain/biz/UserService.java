package agentmain.biz;

public class UserService {


    public UserService() {
        System.out.println("user service init.....");
    }

    public String username = "nero";
    /**
     * @Description:  用于测试，获取用户名
     * @Param: []
     * @return: java.lang.String
     * @Author: zhanglei
     * @Date: 2020/7/9
     */
    public String getUserName(){
       return username;
    }


}
