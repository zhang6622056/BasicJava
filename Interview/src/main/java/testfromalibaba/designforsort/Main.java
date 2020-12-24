package testfromalibaba.designforsort;

import com.alibaba.fastjson.JSON;
import testfromalibaba.designforsort.compare.CompareBase;
import testfromalibaba.designforsort.compare.CompareBaseCallOutOrIn;
import testfromalibaba.designforsort.compare.CompareBaseCreateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {
    private static List<MobileNumber> listMobile = new ArrayList<>();
    private static CompareBase compareChain;


    private static void generateData(){
        //- generate test data..
        Random random = new Random();
        for (int i = 0 ; i < 10 ; i ++){
            MobileNumber mobileNumber = new MobileNumber();
            mobileNumber.setNumber("1880122247"+i);
            if (i < 5){
                mobileNumber.setCallOutTimes(1);
                mobileNumber.setCallInTimes(random.nextInt(9));
            }else{
                mobileNumber.setCallInTimes(1);
                mobileNumber.setCallOutTimes(random.nextInt(9));
            }
            mobileNumber.setCreateTime(new Date(System.currentTimeMillis() + random.nextInt(1000000)));
            listMobile.add(mobileNumber);
        }


        //- link compare rule
        compareChain = new CompareBaseCallOutOrIn();
        CompareBase compareBaseIn = new CompareBaseCallOutOrIn();
        CompareBase compareBaseCreateDate = new CompareBaseCreateTime();
        compareChain.setNext(compareBaseIn);
        compareBaseIn.setNext(compareBaseCreateDate);
    }






    public static void main(String[] args) {
        generateData();
        //- sort
        CompareUtil.sort(listMobile,compareChain);
        //output
        for (MobileNumber mobileNumber : listMobile){
            System.out.println(JSON.toJSONString(mobileNumber));
        }
    }






}
