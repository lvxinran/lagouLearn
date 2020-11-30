package cn.com.lxr.dao;

import cn.com.lxr.bean.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lvxinran
 * @date 2020/7/28
 * @discribe
 */
public interface TokenDao extends JpaRepository<TokenEntity,Integer> {
}
