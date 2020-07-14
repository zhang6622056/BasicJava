package javanero.util;

import java.io.IOException;
import java.util.Timer;

public class TimerPri {

    public static void main(String[] args) throws IOException {
        Timer timer = new Timer("monitor-timer",true);

        //- 第一次执行为500ms之后，再之后的间隔为5000一次
        timer.scheduleAtFixedRate(new AliveModule(),500l,5000l);
        System.in.read();
    }
}
