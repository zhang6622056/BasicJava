package lambdas.basic.j8stream.src;


/****
 *
 *
 * @param <P_OUT>
 */
abstract class PipelineHelper<P_OUT> {


    /****
     * 获取stream类型，共包含4种类型
     * 分别为 reference,int,double,long等
     * @return
     */
    abstract StreamShape getSourceShape();


    /****
     * 获取当前操作类型，个人认为这个int返回值将会是位运算的结果，结果将会是一个标示
     * @return
     */
    abstract int getStreamAndOpFlags();


    /****
     * TODO-ZL 需要知道spliterator具体都做了什么
     * 接受一个spliterator，用于计算具体的容器stream size
     * 如果知道具体的size，返回具体的size，否则返回-1
     * @param <P_IN>
     * @return
     */
    abstract<P_IN> long exactOutputSizeIfKnown(Spliterator<P_IN> spliterator);


























}
