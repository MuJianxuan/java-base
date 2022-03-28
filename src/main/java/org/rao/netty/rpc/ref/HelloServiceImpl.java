package org.rao.netty.rpc.ref;

/**
 * @author Rao
 * @Date 2021/12/21
 **/
public class HelloServiceImpl implements HelloService {


    @Override
    public String hello(String name) {

        //try {
        //    Thread.sleep(20000);
        //} catch (InterruptedException e) {
        //}

        if( "ex".equals(name)){
            throw new RuntimeException("ex");
        }

        return "hello " + name;
    }
}
