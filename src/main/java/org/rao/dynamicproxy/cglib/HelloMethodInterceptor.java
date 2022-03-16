package org.rao.dynamicproxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Rao
 * @Date 2021-10-19
 **/
public class HelloMethodInterceptor implements MethodInterceptor {

    /**
     *
     * @param o  代理对象
     * @param method 执行方法对象 (源类)
     * @param objects 入参
     * @param methodProxy 方法代理
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println( "cglib pre execute ....");
        // c 大坑
        // method.invoke( o,objects); 这个是错误的
        // 因为 cglib 是基于 继续代理类实现的 因此,需要调用 方法代理执行 超类的调用
        Object invoke = methodProxy.invokeSuper(o, objects);
        System.out.println( "cglib post execute ....");
        return invoke;
    }
}
