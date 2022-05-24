package com.webserver.controller;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.entity.Article;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理与文章相关的业务
 */
@Controller
public class ArticleController {
    private static File articleDir;
    static {
        articleDir = new File("./articles");
        if(!articleDir.exists()){
            articleDir.mkdirs();
        }
    }


    /**
     * 发表文章
     * @param request
     * @param response
     */
    @RequestMapping("/myweb/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){
        //1获取表单数据
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        System.out.println(title+","+author+","+content);

        //2保存文章内容
        Article article = new Article(title,author,content);
        File articleFile = new File(articleDir,title+".obj");
        try (
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(articleFile)
                )
        ){
            oos.writeObject(article);
            //3响应发表结果页面
            response.sendRedirect("/myweb/article_success.html");

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @RequestMapping("/myweb/showAllArticle")
    public void showAllArticle(HttpServletRequest request,HttpServletResponse response){
        //1将articles目录下的所有obj文件进行反序列化，得到所有的文章信息
        List<Article> articleList = new ArrayList<>();
        File[] articles = articleDir.listFiles(f->f.getName().endsWith(".obj"));
        for(File articleFile : articles){
            try (
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(
                                    articleFile
                            )
                    )
            ){
                Article article = (Article)ois.readObject();
                articleList.add(article);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /*
            2生成一个HTML页面(动态页面)并包含所有文章数据
         */
        //添加响应头，用于将来告知浏览器该动态数据是一个html页面
        response.setContentType("text/html");

        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("<meta charset=\"UTF-8\">");
        pw.println("<title>文章列表</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("<h1>文章列表</h1>");
        pw.println("<table border=\"1\">");
        pw.println("<tr>");
        pw.println("<td>标题</td>");
        pw.println("<td>作者</td>");
        pw.println("</tr>");
        for(Article article : articleList) {
            pw.println("<tr>");
            pw.println("<td>"+ article.getTitle()+"</td>");
            pw.println("<td>"+ article.getAuthor()+"</td>");
            pw.println("</tr>");
        }
        pw.println("</table>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");
    }


}
