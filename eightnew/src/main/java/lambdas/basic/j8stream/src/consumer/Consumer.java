package lambdas.basic.j8stream.src.consumer;

import java.util.Objects;

@FunctionalInterface
public interface Consumer<T> {



    void accept(T t);

    /****
     * 返回一个函数对象，相当于一个Consumer对象，内部实现了accept方法
     * @param after
     * @return
     */
    default Consumer<T> andThen(Consumer after){
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t);};


        //相当于这个
//        return new Consumer<T>() {
//            @Override
//            public void accept(T t) {
//                accept(t);
//                after.accept(t);
//            }
//        };



    }

}
