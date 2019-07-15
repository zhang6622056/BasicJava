package chapter02;



/**
 *
 * 基于内存可见性问题，由于x没有volitale修饰，那么应该引发内存刷新不及时的问题，导致没有按照预期单线程的执行，
 * TODO 没有遇到错误的情况
 * @author Nero
 * @date 2019-07-15
 * *@param: null
 * @return
 */
public class Chapter2 implements Runnable{

    int x = 0;
    @Override
    public void run() {
        x = x + 1;
    }
}
