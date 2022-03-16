package org.rao.random;

import org.rao.util.IdWorker;
import org.rao.util.Snowflake;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Rao
 * @Date 2021/11/09
 **/
public class RandomMain {

    public static void main(String[] args) {

//        Random random = new Random();

        IdWorker idWorker = new IdWorker(1L);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000000; i++) {
//            random.nextInt(10);
//            test();
            idWorker.nextId();

        }

        long endTime = System.currentTimeMillis();
        System.out.println("time:" + (  endTime - startTime ) );

//        random = new SecureRandom();

        Snowflake snowflake = new Snowflake(1L, 1L, System.currentTimeMillis());

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
//            random.nextInt(10);

            snowflake.nextId();

//            test1();

        }

        endTime = System.currentTimeMillis();
        System.out.println("time:" + (  endTime - startTime ) );



    }

    static void test(){
        try {
            throw new ServiceException("i");
        }catch ( Exception e){}

    }

    static void test1(){
        try {
            throw new ImportantException("i");
        }catch ( Exception e){}
    }


}
