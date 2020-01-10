package another.chapter01;




/***
 *
 * double check 【会有可能】 因为指令重拍和线程切换导致空指针
 * @author Nero
 * @date 2020-01-08
 * *@param: null
 * @return 
 */
public class Singleton {

    private static Singleton instance;

    private String username = null;


    public Singleton() {
        this.username = "123";
    }

    static Singleton getInstance(){
        if (instance == null){
            synchronized(Singleton.class){
                if (instance == null){
                    //- 在内存上申请地址m
                    //- instance = &M
                    //----------发生切换，另外一个线程获取到instance对象，
                    //----------但是instance对象还未初始化完毕，
                    //---------- 这个时候去引用则会触发空指针异常
                    //- M = Singleton对象
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }


    public String getUsername() {
        return username;
    }
}
