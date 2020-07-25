package cn.com.lxr.dao;

import cn.com.lxr.entity.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lvxinran
 * @date 2020/7/20
 * @discribe
 */
public interface CodeDao extends JpaRepository<AuthCode,Integer> {


}
