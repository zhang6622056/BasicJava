package testorder;

import org.junit.*;
import org.junit.runner.RunWith;


public class JunitTestOrder {


    @BeforeClass
    public static void beforeClass(){
        System.out.println("----BeforeClass----");
    }



    @Before
    public void before(){
        System.out.println("----before----");
    }



    @Test
    public void test(){
        //- 断言关键字
        assert true;



        System.out.println("test");
    }



    @Test
    public void test1(){
        System.out.println("test1");
    }





    @After
    public void after(){
        System.out.println("----After----");
    }


    @AfterClass
    public static void afterClass(){
        System.out.println("----afterClass----");
    }
















}
