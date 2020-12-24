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
    private static List<CompareBase> listCompare = new ArrayList<>();


    private static void generateData(){
        //- generate test data..
        Random random = new Random();
        for (int i = 0 ; i < 10 ; i ++){
            MobileNumber mobileNumber = new MobileNumber();
            mobileNumber.setNumber("1880122247"+i);
            mobileNumber.setCallInTimes(random.nextInt(9));
            mobileNumber.setCallOutTimes(random.nextInt(9));
            mobileNumber.setCreateTime(new Date(System.currentTimeMillis() + random.nextInt(1000000)));
            listMobile.add(mobileNumber);
        }


        //- link compare rule

        CompareBase compareBaseOut = new CompareBaseCallOutOrIn();
        CompareBase compareBaseIn = new CompareBaseCallOutOrIn();
        CompareBase compareBaseCreateDate = new CompareBaseCreateTime();
        listCompare.add(compareBaseOut);
        listCompare.add(compareBaseIn);
        listCompare.add(compareBaseCreateDate);
    }






    public static void main(String[] args) {
        //- sort
        CompareUtil.sort(listMobile,listCompare);
        //output
        for (MobileNumber mobileNumber : listMobile){
            System.out.println(JSON.toJSONString(mobileNumber));
        }
    }






}
