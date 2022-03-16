package org.rao;

import org.rao.dynamicproxy.DaoProxyHandler;
import org.rao.dynamicproxy.ProxyHandler;
import org.rao.dynamicproxy.SqlFactory;
import org.rao.dynamicproxy.dao.UserDao;
import org.rao.dynamicproxy.original.SayHello;
import org.rao.dynamicproxy.original.SayHelloImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 1、动态代理
 * @author Rao
 * @Date 2021-10-18
 **/
public class JdkProxyMain {

    public static void main(String[] args) throws Exception {

        // 基于方法对象的反射调用 思考问题： 获取方法对象途径？
        // 1、Class
        Method whoMethod = Class.forName(SayHello.class.getCanonicalName()).getMethod("who");
        Object invoke = whoMethod.invoke(new SayHelloImpl());
        System.out.println( invoke);


        // 基于 对象代理
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        SayHello sayHelloProxy = (SayHello)Proxy.newProxyInstance( JdkProxyMain.class.getClassLoader(), SayHelloImpl.class.getInterfaces(), new ProxyHandler( new SayHelloImpl() ));
        sayHelloProxy.say("sayHelloProxy");

        // 基于 接口代理
        // 猜想：Mybatis 的 dao接口是如何实现访问数据库的？
        // 1、先了解mybatis 基于接口调用数据库的本质，dao都是接口方法，本质是仅获取方法的返回对象、方法名，类名和入参值，那么问题很清晰了；
        // 并不需要实际的执行对象来执行 方法的实现，仅需要获取到 相应的信息，然后调用其他对象执行后返回即可，但我们应该拆离出来，就拿上面的 ProxyHandler 来说
        // 核心执行方法 是在 ProxyHandler 类中，如果将业务代码冗余进去此类，这个类就会显得笨重，违反单一职责原则。
//        Proxy.newProxyInstance( )
        /**
         * 在测试之前可能需要把玩几个例子
         */
        //1 此执行会抛异常, 由此可见,动态代理需要关注的是  后两个参数, 第一个参数取决与 类加载环境,
//        SayHello sayHelloProxy1 = (SayHello)Proxy.newProxyInstance(SayHelloImpl.class.getClassLoader(), SayHello.class.getInterfaces(), new ProxyHandler(new SayHelloImpl()));
//        SayHello sayHelloProxy11 = (SayHello)Proxy.newProxyInstance(SayHello.class.getClassLoader(), SayHello.class.getInterfaces(), new ProxyHandler(new SayHelloImpl()));

        //2 可以执行
        SayHello sayHelloProxy2 = (SayHello)Proxy.newProxyInstance(JdkProxyMain.class.getClassLoader(), SayHelloImpl.class.getInterfaces(), new ProxyHandler(new SayHelloImpl()));
        sayHelloProxy2.say("sayHelloProxy2");

        //3 可以执行  可见真正影响 动态代理类型的是 第二个参数
        SayHello sayHelloProxy3 = (SayHello)Proxy.newProxyInstance(JdkProxyMain.class.getClassLoader(), new Class[]{SayHello.class}, new ProxyHandler(new SayHelloImpl()));
        sayHelloProxy3.say("sayHelloProxy3");

        // 探讨第二个参数: 获取的是 这个类实现的接口集合,是一个 类数组 类型,因此我们通过自定义的 接口类 数组 也可以进行执行,也就是 3 个 eg
        System.out.println(Arrays.toString(SayHelloImpl.class.getInterfaces()));

        // 问题来到 第3个参数, 是一个 InvocationHandler 的实现 ,其里面封装了 执行逻辑, 包括是否执行 ( 目标方法 ) ,答案很明显了

        SqlFactory sqlFactory = new SqlFactory();
        UserDao userDao = (UserDao) Proxy.newProxyInstance(DaoProxyHandler.class.getClassLoader(), new Class[]{UserDao.class}, new DaoProxyHandler(UserDao.class, sqlFactory));
        String nameById = userDao.findNameById(1L);


        // Jdk 动态代理是否可以 对无接口类实现动态代理呢?
//        HelloCglibService helloCglibService = (HelloCglibService)Proxy.newProxyInstance(HelloCglibService.class.getClassLoader(),HelloCglibService.class.getInterfaces(),new ProxyHandler(new HelloCglibService()));
//        helloCglibService.hello("jdk");

//        Object helloCglibService = Proxy.newProxyInstance(HelloCglibService.class.getClassLoader(),HelloCglibService.class.getInterfaces(),new ProxyHandler(new HelloCglibService()));
//        System.out.println(helloCglibService.getClass());
//        helloCglibService.hashCode();

        // 是否可以 我仅切某个方法?
        // 在 ProxyHandler 类中 判断是否是代理方法,是的话,才执行代理的业务逻辑,否则即调用原方法.
        System.out.println( sayHelloProxy.who()) ;
    }

}
