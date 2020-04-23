## 编程题：个人博客系统首页展示（文章分页展示）

   前台需要显示： 首页 上一页 下一页 尾页

答：项目在springboot_thymeleaf目录中

#### 测试步骤：

1、首先使用**localhost:8080/show**访问主页（注：此处使用重定向技术，自动跳转到/main?page=1第一页，使用flash方式）

![image-20200423165820073](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200423165820073.png)

每页条数在application.properties里面设置，可以更改，使用@Value注解实现，在Page对象中设置，不是写死的

![image-20200423165933899](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200423165933899.png)

2、如果为第一页，则上一页和首页不可点击，如果为最后一页，则下一页和尾页不可点击

## 代码片段：

用来实现分页的Page对象

```java
public class Page {
    private int currentPage=1;    //当前页数
    private int totalPages;       //总页数
    private int totalArticle;//记录总行数

    @Value("${page.size}")
    private int pageSize=2;    //每页记录行数
    private int nextPage;        //下一页
    private int prefPage;       //前一页

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        totalPages = totalArticle % pageSize == 0 ? totalArticle / pageSize : totalArticle / pageSize + 1;

        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalArticle() {
        return totalArticle;
    }

    public void setTotalArticle(int totalArticle) {
        this.totalArticle = totalArticle;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNextPage() {
        if (currentPage < totalPages) {
            nextPage = currentPage + 1;
        } else {
            nextPage = currentPage;
        }
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrefPage() {
        if (currentPage > 1) {
            prefPage = currentPage - 1;
        } else {
            prefPage = currentPage;
        }
        return prefPage;
    }

    public void setPrefPage(int prefPage) {
        this.prefPage = prefPage;
    }
}
```

Controller对象

```java
public class LoginController {


    @Autowired
    private ArticleService articleService;

    @Autowired
    Page p;
    @RequestMapping(value = "/main",method = RequestMethod.GET)
    public String toMainPage(Model model,int page){
        p.setTotalArticle(articleService.findAllArticle().size());
        p.setCurrentPage(page);
        List<Article> articleList = articleService.findByPage(page - 1,p.getPageSize());
        model.addAttribute("allArticle",articleList);
        model.addAttribute("currPage",p);
        return "index";
    }
    @RequestMapping(value = "/show",method = RequestMethod.GET)
    public String goToMainPage(RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page",1);
        return "redirect:main";
    }
}
```

实现时间显示的th标签

```html
<div class="am-u-md-8 am-u-sm-12">

    <!-- 文章遍历并分页展示 : 需要同学们手动完成，基本样式已经给出，请使用th标签及表达式完成页面展示 -->
    <div th:each="article : ${allArticle}">
        <article class="am-g blog-entry-article">

            <div class="am-u-lg-6 am-u-md-12 am-u-sm-12 blog-entry-text">
                <!-- 文章分类 -->
                <span class="blog-color"style="font-size: 15px;"><a>默认分类</a></span>
                <span>&nbsp;&nbsp;&nbsp;</span>
                <!-- 发布时间 -->
                <span style="font-size: 15px;" th:text="'发布于 '+ ${article.getCreated()}" />
                <h2>
                    <!-- 文章标题 -->
                    <div><a style="color: #0f9ae0;font-size: 20px;" th:text="${article.getTitle()}" />
                    </div>
                </h2>
                <!-- 文章内容-->
                <div style="font-size: 16px;" th:text="${article.getContent()}" />
            </div>
        </article>
    </div>
```

实现分页的代码

```java
<div>
    <span th:if="${currPage.getCurrentPage()} eq '1'" class="grey" >
        首页
    </span>
    <a th:if="${currPage.getCurrentPage()} ne '1'" th:href="@{/main(page=1)}">首页</a>

    <span th:if="${currPage.getCurrentPage()} eq '1'" class="grey">
        上一页
    </span>
    <a th:if="${currPage.getCurrentPage()} ne '1'" th:href="@{/main(page=${currPage.getPrefPage()})}">上一页</a>

    <span>当前第<span th:text="${currPage.getCurrentPage()}" class="grey">1</span>页</span>
    <span th:if="${currPage.getCurrentPage()} eq ${currPage.getTotalPages()}" class="grey">
        下一页
    </span>
    <a th:if="${currPage.getCurrentPage()} ne ${currPage.getTotalPages()}" th:href="@{/main(page=${currPage.getNextPage()})}">下一页</a>

    <span th:if="${currPage.getCurrentPage()} eq ${currPage.getTotalPages()}" class="grey">
        尾页
    </span>
    <a th:if="${currPage.getCurrentPage()} ne ${currPage.getTotalPages()}" th:href="@{/main(page=${currPage.getTotalPages()})}">尾页</a>

</div>
```