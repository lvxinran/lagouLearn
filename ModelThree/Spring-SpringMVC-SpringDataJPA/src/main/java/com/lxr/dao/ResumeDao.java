package com.lxr.dao;

import com.lxr.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author lvxinran
 * @date 2020/4/16
 * @discribe JpaRepository封装了curd
 *           JpaSpecificationExecutor封装了复杂查询
 */
public interface ResumeDao extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {

}
