package com;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;




/***
 *
 * while(true) A账户与B账户总是尝试获取对方的锁
 * @author Nero
 * @date 2020-01-09
 * *@param: null
 * @return 
 */
public class UserAccount {
    public ReentrantLock reentrantLock = new ReentrantLock();
    public BigDecimal balance;


    //- 转账
    public void transferTo(BigDecimal amount, UserAccount targetUser){
        while(true){
            //- 尝试获取自己账户的锁
            if (reentrantLock.tryLock()){
                try{
                    //- 尝试获取对方账户的锁
                    if (targetUser.reentrantLock.tryLock()){
                        try{
                            balance = this.balance.subtract(amount);
                            targetUser.balance = targetUser.balance.add(amount);
                        }finally {
                            targetUser.reentrantLock.unlock();
                        }
                    }
                }finally{
                    reentrantLock.unlock();
                }
            }
        }
    }
}
