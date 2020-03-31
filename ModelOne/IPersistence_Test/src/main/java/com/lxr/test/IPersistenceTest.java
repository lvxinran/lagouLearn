package com.lxr.test;

import com.lxr.dao.IUserDao;
import com.lxr.io.Resources;
import com.lxr.pojo.User;
import com.lxr.sqlSession.SqlSession;
import com.lxr.sqlSession.SqlSessionFactory;
import com.lxr.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class IPersistenceTest {


    private  IUserDao userDao;

    @Before
    public void before() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = factory.openSession();
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @Test
    public void test(){

        User user = new User();
        user.setId(1);
        user.setUsername("lxr");
//        User user1=userDao.findOneByCondition(user);
        List<User> users = userDao.findAll();
        System.out.println(users);
    }
    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(16);
        user.setUsername("lxr");
        int i = userDao.updateOne(user);
        System.out.println(i>0?"更新成功":"更新失败");
    }
    @Test
    public void testDelete() {
        int i = userDao.deleteOne(1);
        System.out.println(i>0?"删除成功":"删除失败");
    }
    @Test
    public void testInsert() {
        User user = new User();
        user.setId(16);
        user.setUsername("insertLxr");
        int i = userDao.insertOne(user);
        System.out.println(i>0?"添加成功":"添加失败");
    }
}
