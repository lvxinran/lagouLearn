import com.lxr.dao.ResumeDao;
import com.lxr.pojo.Resume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ResumeDaoTest {

    @Autowired
    private ResumeDao resumeDao;

    @Test
    public void testFindById(){

        Optional<Resume> optional = resumeDao.findById(1L);
        Resume resume = optional.get();
        System.out.println(resume);
    }
    @Test
    public void testFindOne(){

        Resume resume = new Resume();
        resume.setId(1L);
        Optional<Resume> optional = resumeDao.findOne(Example.of(resume));
        System.out.println(optional.get());
    }

}
