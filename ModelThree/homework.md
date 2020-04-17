## 作业一：

### 手写MVC框架基础上增加如下功能

1）定义注解@Security（有value属性，接收String数组），该注解用于添加在Controller类或者Handler方法上，表明哪些用户拥有访问该Handler方法的权限（注解配置用户名）

2）访问Handler时，用户名直接以参数名username紧跟在请求的url后面即可，比如http://localhost:8080/demo/handle01?username=zhangsan

3）程序要进行验证，有访问权限则放行，没有访问权限在页面上输出

注意：自己造几个用户以及url，上交作业时，文档提供哪个用户有哪个url的访问权限

答：代码在mvc目录下

```java
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
	...


        // 根据uri获取到能够处理当前请求的hanlder（从handlermapping中（list））
        Handler handler = getHandler(req);
        //权限校验
        Method method = handler.getMethod();
        Security controllerAnnotation = method.getDeclaringClass().getAnnotation(Security.class);
        Security handlerAnnotation = method.getAnnotation(Security.class);
        boolean check = SecurityCheck(req,controllerAnnotation)&&SecurityCheck(req,handlerAnnotation);
        if(!check){
            resp.getWriter().write("权限不符，无法请求");
            return;
        }
	...
    }
```

```java
private boolean SecurityCheck(HttpServletRequest req,Security security) {
    if(security==null){
        return true;
    }
    String[] secNames= security.value();
    if(secNames!=null&&secNames.length>0){
        String name = req.getParameter("name");
        for(String sec:secNames){
            if(!sec.equals(name)){
                continue;
            }
            return true;
        }
    }
    return false;
}
```

```java
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
    String[] value();
}
```

测试类：只有类和方法上都有该名字时才会校验通过，如果没有注解默认通过（lxr通过，lxp不通过，lxy不通过，因为只有lxr是在两个注解中都有的）

```java
@LagouController
@Security({"lxr"})
@LagouRequestMapping("/demo")
public class DemoController {


    @LagouAutowired
    private IDemoService demoService;


    /**
     * URL: /demo/query?name=lisi
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LagouRequestMapping("/query")
    @Security({"lxr","lxp"})
    public String query(HttpServletRequest request, HttpServletResponse response,String name) {
        return demoService.get(name);
    }
}
```

## 作业二：

需求：实现登录页面（简易版即可），实现登录验证功能、登录之后跳转到列表页，查询出 tb_resume 表【表数据和课上保持一致】的所有数据（列表不要求分页，在列表右上方有“新增”按钮，每一行后面有“编辑”和“删除”按钮，并实现功能），如果未登录就访问url则跳转到登录页面，用户名和密码固定为admin/admin

技术要求：根据SSM整合的思路，进行SSS整合（Spring+SpringMVC+SpringDataJPA）,登录验证使用SpringMVC拦截器实现

【提交时统一数据库名test，用户名和密码root】

答：

代码在\ModelThree\Spring-SpringMVC-SpringDataJPA下

#### 大致流程

- spring整合springdataJPA
- spring整合springmvc
- 编写配置文件applicationContext.xml、springmvc.xml
- 编写jsp页面，Controller层和service层

列举关键代码

service层，更新和添加用同一方法

```java
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
```

登录拦截器，判断session中是否有信息，没有则进行拦截

```java
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        //UTL:除了login.jsp是可以公开访问的，其他的URL都进行拦截控制
        if (uri.indexOf("/login") >= 0) {
                 return true;
        }//获取session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");
         //判断session中是否有用户数据，如果有，则返回true，继续向下执行
         if (user != null) {
                 return true;
             }
        //不符合条件的给出提示信息，并转发到登录页面
        request.setAttribute("msg", "您还没有登录，请先登录！");
        try {
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
```