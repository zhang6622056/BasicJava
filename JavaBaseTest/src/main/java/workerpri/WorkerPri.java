package workerpri;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class WorkerPri {


    public static void main(String[] args) throws IOException {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("running....");
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,1000,1000);
        System.in.read();
    }




}
