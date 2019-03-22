package lambdas.basic.maininterface;

/***
 * 接受一个T类型的参数，返回一个boolean类型的结果
 */
public interface Predicate<T> {
    boolean judge(T t);
}
