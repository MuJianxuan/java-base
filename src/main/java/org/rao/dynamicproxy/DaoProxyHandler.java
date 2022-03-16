package org.rao.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 可以利用继承 ?  no
 * 应该使用 委托
 * @author Rao
 * @Date 2021-10-19
 **/
public class DaoProxyHandler implements InvocationHandler {

    // 需要传入 类信息吗?  可以不用
    // 通过 method.getDeclaringClass().getName() 获取
    private Class<?> originalClass;
    private SqlFactory sqlFactory;

    public DaoProxyHandler(Class<?> originalClass,SqlFactory sqlFactory) {
        this.originalClass = originalClass;
        this.sqlFactory = sqlFactory;
    }

    /**
     * 入参
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // mybatis 的 sql key 是基于 类名+方法名 因此我们得获取类信息
//        method.getClass()  猜想 获取到的类是接口类吗?  有可能获取到的是 代理类吧?
        System.out.println( method.getClass().getName());
        // 这个可以获取到 类
        System.out.println( method.getDeclaringClass().getName());

        // 获取方法名
        String methodName = method.getName();
        System.out.println( "执行Sql:" + sqlFactory.getSql( originalClass.getName()+methodName) );

        // 获取返回类型
        Class<?> returnType = method.getReturnType();

        System.out.println( proxy.getClass().getTypeName());

        return null;
    }
}
