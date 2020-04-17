import com.lxr.pojo.Account;
import com.lxr.service.AcccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/16
 * @discribe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class MybatisSpringTest {

    @Autowired
    private AcccountService acccountService;
    @Test
    public void testMybatis(){
        List<Account> accounts = acccountService.queryAccountList();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println(account);
        }
    }
}
