package org.rao.dynamicproxy.original;

import org.rao.dynamicproxy.dao.UserDao;

/**
 * @author Rao
 * @Date 2021-10-18
 **/
public class SayHelloImpl implements SayHello, UserDao {


    @Override
    public void say(String name) {
        System.out.println("hello "+ name);
    }

    @Override
    public String who() {
        return "Me";
    }

    @Override
    public String findNameById(Long id) {
        return null;
    }
}
