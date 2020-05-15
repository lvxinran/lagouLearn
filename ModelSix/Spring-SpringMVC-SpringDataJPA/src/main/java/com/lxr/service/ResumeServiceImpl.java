package com.lxr.service;

import com.lxr.dao.ResumeDao;
import com.lxr.pojo.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeDao resumeDao;
    @Override
    public List<Resume> findAll() {
        return resumeDao.findAll();
    }

    @Override
    public int updateOne(Resume resume) {
        Resume save = resumeDao.save(resume);
        if(save!=null){
            return 1;
        }
        return 0;
    }

    @Override
    public Resume findById(long id) {
        Optional<Resume> resume = resumeDao.findById(id);
        return resume.get();
    }

    @Override
    public void deleteOne(long id) {
        resumeDao.deleteById(id);
    }
}
