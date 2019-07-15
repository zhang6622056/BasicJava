package chapter14;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/***
 *
 * 活锁程序验证
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return
 */
public class Account {

    private BigDecimal balance = BigDecimal.valueOf(100000);
    private String username;


    private final Lock balanceLock = new ReentrantLock();


    /***
     * 增加余额
     * @author Nero
     * @date 2019-07-15
     * *@param: target
     *@param: forAdd
     * @return void
     */
    public void addBalance(Account target,BigDecimal forAdd){
        try{
            balanceLock.tryLock();
            Lock tarLock = target.getBalanceLock();
            try{
                tarLock.tryLock();


                if (balance.compareTo(forAdd) == -1){
                    System.out.println("余额不足扣除...");
                    return;
                }
                balance = balance.subtract(forAdd);
                target.setBalance(target.getBalance().add(forAdd));

                System.out.println("本地账户余额"+this.balance+"，target 余额："+target.getBalance());

            }finally{
                tarLock.unlock();
            }
        }finally{
            balanceLock.unlock();
        }
    }









    private Lock getBalanceLock() {
        return balanceLock;
    }

    private BigDecimal getBalance() {
        return balance;
    }

    private void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    private String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }
}
