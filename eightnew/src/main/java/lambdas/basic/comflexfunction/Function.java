package lambdas.basic.comflexfunction;

public interface Function<T,R> {
    R apply(T t);

    default <V> Function<V,R> compose(Function<? super V,? extends R> before){
        return (V v) -> before.apply(v);
    }
}
