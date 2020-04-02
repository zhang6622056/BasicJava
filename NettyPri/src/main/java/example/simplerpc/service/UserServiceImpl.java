package example.simplerpc.service;

public class UserServiceImpl implements UserService{
    @Override
    public String getUsername() {
        return "nero";
    }



    @Override
    public String getPwd() {
        return "123456";
    }
}
