package com.lxr.service;

import com.lxr.pojo.Resume;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
public interface ResumeService {

    List<Resume> findAll();

    int updateOne(Resume resume);

    Resume findById(long id);

    void deleteOne(long id);
}
