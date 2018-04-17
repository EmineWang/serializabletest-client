package com.yanming.test.hessian;

import com.caucho.hessian.io.Hessian2Output;
import com.yanming.test.dto.Person;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class HessianTest {

    public static String urlName = "http://localhost:8080/hessian/hello";

    public static void main(String[] args) throws Throwable {

        // 序列化
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output h2o = new Hessian2Output(os);
        h2o.startMessage();
        h2o.writeObject(getPerson());
        h2o.writeString("I am client.");
        h2o.completeMessage();
        h2o.close();

        byte[] buffer = os.toByteArray();
        os.close();
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(buffer,
                ContentType.create("x-application/hessian", "UTF-8"));

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
