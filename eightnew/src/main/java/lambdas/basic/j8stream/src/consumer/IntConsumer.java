package lambdas.basic.j8stream.src.consumer;

import java.util.Objects;

@FunctionalInterface
public interface IntConsumer {
    void accept(int value);

    default IntConsumer andThen(IntConsumer after){
        Objects.requireNonNull(after);
        return (int t) -> {accept(t); after.accept(t);};
    }
}
