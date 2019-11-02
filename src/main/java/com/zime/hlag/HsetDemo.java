package com.zime.hlag;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class HsetDemo {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost");
        Post post=new Post();
        post.setAuthor("hlag");
        post.setContent("fdfsf");
        post.setTitle("f");
        post.setTime("2019年10月28日 10:12:28");

        Long postId=savePost(post,jedis);
        System.out.println("保存成功");

        Post myPost = getPost(postId,jedis);
        System.out.println(myPost);
        System.out.println("获取成功");
    }

    private static Post getPost(Long postId, Jedis jedis) {
         Map<String,String> myBlog=jedis.hgetAll("post:"+postId+":data");

         Post post=new Post();
         post.setTitle(myBlog.get("title"));
         post.setAuthor(myBlog.get("author"));
         post.setContent(myBlog.get("content"));

         return post;
     }

    private static Long savePost(Post post, Jedis jedis){
        Long postId =jedis.incr("posts");
        Map<String,String> blog=new HashMap<String, String>();
        blog.put("title",post.getTitle());
        blog.put("author",post.getAuthor());
        blog.put("content",post.getContent());
        jedis.hmset("post"+postId+":date",blog);
        System.out.println("post:"+postId+":data");
        System.out.println(jedis.hgetAll("post"+postId+":date"));

        return postId;
    }
}
