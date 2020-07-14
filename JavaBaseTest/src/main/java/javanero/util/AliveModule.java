package javanero.util;

import java.util.TimerTask;


/****
 * 自定义一个任务，在Timer中执行
 */
public class AliveModule extends TimerTask {
    @Override
    public void run() {
        System.out.println("the AliveModule is running...");
    }
}
