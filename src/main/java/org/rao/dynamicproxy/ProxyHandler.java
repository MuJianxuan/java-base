package org.rao.dynamicproxy;

import org.rao.dynamicproxy.original.SayHello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 静态代理
 * @author Rao
 * @Date 2021-10-18
 **/
public class ProxyHandler implements InvocationHandler {

    /**
     * 原对象
     */
    private Object proxyObject;
    public ProxyHandler(Object object) {
        this.proxyObject = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if( method.getName().equals("who")){
            return method.invoke( proxyObject, args);
        }

        System.out.println("pre execute");
        Object invoke = method.invoke(proxyObject, args);
        System.out.println("post execute");

        // 代理对象
//        SayHello proxyObj = (SayHello) proxy;
        // 这里执行会触发递归   代理对象执行 方法 本质是 执行 当前方法，也就是代理 是做了两层，
        // 1、第一层是 当前对象， 包含了执行逻辑与 代理执行
        // 2、第二层就是 代理对象本身，思考，如何使用一个对象完成所有方法的执行代理。 先从静态代理思考，
        //  静态代理需要对每一个方法编写前置和后置处理，而如何做到对过程执行做到切面，
        // AOP 技术的本质是 解决过程执行中重复动作的问题；那么动态代理是如何实现的？
        //   通过生成代理类 注入到 虚拟机中来返回类对象,通过 类对象 创建对象的newInstance返回代理对象.
        // proxyObj.say();


        return invoke;
    }
}
