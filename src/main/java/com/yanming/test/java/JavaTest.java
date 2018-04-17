package com.yanming.test.java;

import com.yanming.test.dto.Person;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author yanming
 * @version 1.0.0
 * @description
 * @date 2018/04/17 15:29
 **/
public class JavaTest {

    public static String urlName = "http://localhost:8080/java/seri";

    public static void main(String[] args) throws Throwable {


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);

        os.writeObject(getPerson());

        os.close();

        byte[] buffer = bos.toByteArray();
        os.close();

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(buffer,
                ContentType.create("x-java-serialized-object", "UTF-8"));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(urlName);
        post.setEntity(byteArrayEntity);
        CloseableHttpResponse response = client.execute(post);

        System.out.println("response status:\n"
                + response.getStatusLine().getStatusCode());
        HttpEntity body = response.getEntity();
        System.out.println("body:"+body);
    }

    public static Person getPerson() {
        Person person = new Person();
        person.setAddress(new String[] { "Beijing", "TaiWan", "GuangZhou" });
        person.setName("Jack");
        person.setPhone(188888888);
        return person;
    }


}
