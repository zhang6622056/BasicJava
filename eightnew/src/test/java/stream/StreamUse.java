package stream;

import lambdas.basic.randoms.RandomBean;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class StreamUse {







    public static class Product{
        private Integer id;
        private Integer name;
        private Integer price;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getName() {
            return name;
        }

        public void setName(Integer name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }
    }



    @Test
    public void test() throws InstantiationException, IllegalAccessException {
        List<Product> list = RandomBean.generateBeanList(Product.class,10l);
        list.forEach(product -> System.out.println(product.getName()));
    }



    @Test
    public void testReflect(){
        Arrays.asList(Product.class.getDeclaredFields()).forEach(
                field -> {
                   // field.getType().getName()
                }
        );
    }





}
