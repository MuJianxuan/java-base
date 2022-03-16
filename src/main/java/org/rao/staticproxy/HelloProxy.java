package org.rao.staticproxy;

/**
 * @author Rao
 * @Date 2021-10-18
 **/
public class HelloProxy implements HelloInterface{
    private HelloInterface helloInterface = new HelloInterfaceImpl();

    @Override
    public void sayHello() {
        System.out.println("Before invoke sayHello" );
        helloInterface.sayHello();
        System.out.println("After invoke sayHello");
    }
}
