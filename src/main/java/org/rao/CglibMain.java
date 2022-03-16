package org.rao;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import org.rao.dynamicproxy.cglib.HelloCglibService;
import org.rao.dynamicproxy.cglib.HelloMethodInterceptor;

/**
 * cglib 动态代理 实践
 *
 * cglib是一个java字节码的生成工具，它动态生成一个被代理类的子类，
 * 子类重写被代理的类的所有不是final的方法。
 * 在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。
 *
 * 十年偶见，往事上心头，醉酒飞歌，决心永退；
 * 而后一年，佳人复游园，复词郁疾，香消玉殒；
 * 四十而后，游翁归故里，愕知往事，梦回香消；
 * 年复一年，园里花似锦，赋诗咏忆，惊鸿照影；
 * 八五将至，奈己已无命，美人幽梦，春游永念。
 *
 * @author Rao
 * @Date 2021-10-19
 **/
public class CglibMain {

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/tmp/cglib");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloCglibService.class);
        // ? 需要留意的是 顶级接口,然后 instance 是 methodxxx ? 估计是
        enhancer.setCallback( new HelloMethodInterceptor() );
        HelloCglibService helloCglibService = (HelloCglibService)enhancer.create();
        helloCglibService.hello("cglib");


        // 实现原理是 通过继承 被代理类,进行改写逻辑. Jdk 动态代理要有意义的话是需要 实现接口的,否则仅对 Object的方法做了代理.
        // 本质思想都是生成 代理类代码后注入虚拟机中返回代理类对象. 此外底层实现不同,jdk通过模板文件生成方式,cglib是利用ASM字节码改写生成子类来处理.
        // 1.8 jdk动态代理的性能已不弱于cglib  在jdk1.8 之前还有不小的差距呢

        /*
        jdk的版本优化中主要是针对虚拟机对反射调用的优化，在jdk1.6中，我们采用JDK代理的方式来生成动态代理类，
        反射方法的调用在15次以内是调用本地方法，即是java到c++代码转换的方法，这种方式比直接生成字节码文件要快的多，
        而在15次之后则开始使用java实现的方式。
        在1.8的版本优化中，反射调用的次数达到阈值[也就是发射调用的类成为热点时]之后采用字节码的方式，
        因为字节码的 方式只有在第一次生成字节码文件时比较消耗时间。简单的说 jdk1.8之后,优化了反射调用方式,采用调用字节码操作了
         */

    }

}
