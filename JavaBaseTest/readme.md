## Junit

###     @BeforeClass @AfterClass

相对于整个执行过程，必须修饰静态方法，对应一次junit test运行。整个过程不论执行多少次。只会执行一次beforeclass修饰的方法，相当于静态代码块.



### @Before  @After

顾名思义，该类型修饰的方法将会在test的前后执行。每一个方法对应一个before和after，有点类似aop。

```
----before----
test
----After----

----before----
test1
----After----
```



### assert

assert [逻辑语句]或者使用Assert静态方法



















