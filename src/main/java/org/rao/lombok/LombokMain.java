package org.rao.lombok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

/**
 * desc: Lombok 注解使用
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@Slf4j
public class LombokMain {
    public static void main(String[] args) {

        // 默认值问题
        User user = new User();
        User build = User.builder().build();

        log.info( "user:{}",user );
        log.info( "user:{}",build );

        // name 属性丢失
        User sunB = Sun.builder().age(33).build();
        Sun build1 = Sun.builder().age(22).name("xxxx").build();

//        log.info( "sun:{}",sun );
        log.info( "sunB:{}",sunB );
        // 父类属性丢失。
        log.info("build1:{}",build1);


        Name value = new Name();
        value.setOf("dd");
        value.setValue("ddd");
        log.info("name:{}",value);


        // 测试 @Accessors 注解 与BeanCopier 的问题
        // spring 的没问题
        BeanCopier beanCopier = BeanCopier.create(Name.class, Name.class, false);
        Name newName = new Name();
        beanCopier.copy( value, newName,null );
        log.info("newName:{}",newName);

        // cglib的 也没问题
        net.sf.cglib.beans.BeanCopier beanCopier1 = net.sf.cglib.beans.BeanCopier.create(Name.class, Name.class, false);
        beanCopier1.copy( value, newName,null );
        log.info("newName:{}",newName);


    }
}
