package lambdas.basic.maininterface;


import java.util.Objects;

/***
 * 输入R ,输出V
 */
public interface Function<K,V> {
    V apply(K k);
}
