package threadpool;

public class FillServiceImpl extends BaseFill{
    @Override
    void fill(UserBean userBean) {
        userBean.setUsername("FillServiceImpl");
        System.out.println("FillServiceImpl");
    }


    public static void main(String[] args) {
        FillServiceImpl fillService = new FillServiceImpl();
        UserBean userBean = new UserBean();
        fillService.fillStart(userBean);
        System.out.println(userBean.getUsername());
    }


}
