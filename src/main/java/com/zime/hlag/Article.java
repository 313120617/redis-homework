package com.zime.hlag;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Article {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost");
        Post post=new Post();
        post.setAuthor("hlag");
        post.setContent("fdfsf");
        post.setTitle("f");
        post.setTime("2019年11月2日 19:06:39");

        Long postId=savePost(post,jedis);
        System.out.println("保存成功");

        Post post1=getPost(jedis,postId);
        System.out.println(post1);
        System.out.println("获取成功");
    }

    private static Long savePost(Post post, Jedis jedis) {
        Long posts =jedis.incr("posts");
        String postStr = JSON.toJSONString(post);
        jedis.set("post:" + posts +"data",postStr);
        return posts;
    }
    private static Post getPost(Jedis jedis, Long postId) {
        String post =jedis.get("post:"+postId+"data");
        Post post1 = JSON.parseObject(post,Post.class);
        return post1;
    }
    public static void delpost(long postId,Jedis jedis){
        jedis.hdel("Post:"+postId,"author");
        jedis.hdel("Post:"+postId,"content");
        jedis.hdel("Post:"+postId,"score");
        jedis.lrem("list:post",1,"Post:"+postId);
    }
    public static void delpost1(long postid, String key, Jedis jedis){
        jedis.hdel("Post:"+jedis.get("postid"),key);
    }
    public static void UpPost(int postid,String key,String val,Jedis jedis){
        Map<String, String> sw = jedis.hgetAll("Post:" + postid);
        sw.put(key,val);
        jedis.hmset("Post:" + postid,sw);
    }
    public static void setpost(long page,long size,Jedis jedis){
        jedis.lrange("list:post",(page-1)*size,page*size-1);
        System.out.println(jedis.lrange("list:post",(page-1)*size,page*size-1));
    }


}
