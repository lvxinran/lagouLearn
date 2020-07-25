package cn.com.lxr.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lvxinran
 * @date 2020/7/28
 * @discribe
 */
@Entity
@Table(name = "lagou_token")
public class TokenEntity {

    @Id
    private int id;

    private String email;

    private String token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
