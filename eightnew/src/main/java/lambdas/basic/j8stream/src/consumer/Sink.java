package lambdas.basic.j8stream.src.consumer;

public interface Sink<T> extends Consumer<T> {


    default void begin(long size) {}
    default void end(){}


    default boolean cancellationRequested() {return false;}


    default void accept(int value){throw new IllegalStateException("called wrong accept method");}
    default void accept(long value){throw new IllegalStateException("called wrong accept method");}
    default void accept(double value){throw new IllegalStateException("called wrong accept method");}










}
