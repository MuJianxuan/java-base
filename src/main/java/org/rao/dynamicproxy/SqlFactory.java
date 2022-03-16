package org.rao.dynamicproxy;

import org.rao.dynamicproxy.dao.UserDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rao
 * @Date 2021-10-19
 **/
public class SqlFactory {

    private static final Map<String,String> SQL_CONTAINER = new HashMap<String, String>();
    static {
        SQL_CONTAINER.put(UserDao.class.getName()+"findNameById","select name from user where id = #{id}");
    }

    public String getSql(String key){
        return Optional.ofNullable( SQL_CONTAINER.get( key)).orElseThrow(() -> new RuntimeException("SQL not exist!"));
    }

}
