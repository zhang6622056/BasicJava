package opencc4j;

import com.github.houbb.opencc4j.util.ZhConverterUtil;

import java.io.UnsupportedEncodingException;

public class Test {
 public static void main(String[] args) throws UnsupportedEncodingException {
  String receiverName = ZhConverterUtil.convertToSimple(new String("陈文煇".getBytes(),"utf-8"),false);
  System.out.println(receiverName);
 }
}
