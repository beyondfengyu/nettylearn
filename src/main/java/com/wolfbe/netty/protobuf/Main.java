package com.wolfbe.netty.protobuf;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wolfbe.netty.protobuf.proto.Person;
import com.wolfbe.netty.protobuf.proto.PersonProbuf;
import com.wolfbe.netty.util.CastHelper;

import java.nio.charset.Charset;

/**
 * 测试protobuf与fastjson编解码的性能
 * 结果：fastjson耗时是protobuf的3倍，压测数量为千万级
 * @author Andy
 */
public class Main {

    private static int TEST_NUM = 1000 * 10000;

    public static void main(String[] args) throws InvalidProtocolBufferException {
        CastHelper helper = CastHelper.castListener();
        PersonProbuf.Person person2 = null;
        for (int i = 0; i < TEST_NUM; i++) {
            PersonProbuf.Person.Builder builder = PersonProbuf.Person.newBuilder();
            builder.setEmail("234234@qq.comhellohellohellohellohellohellohellohello你好你好hellohellohellohellohellohellohellohello你好你好");
            builder.setId(2);
            builder.setName("hellohellohellohellohellohellohellohello你好你好hellohellohellohellohellohellohellohello你好你好");

            PersonProbuf.Person person = builder.build();
            byte[] bytes = person.toByteArray();

            person2 = PersonProbuf.Person.parseFrom(bytes);
        }
        System.out.println(person2.getName() + ", " + person2.getEmail());
        System.out.println("protobuf cast time:" + helper.cast());

        CastHelper helper2 = CastHelper.castListener();
        Person person3 = null;
        for (int i = 0; i < TEST_NUM; i++) {
            Person person = new Person();
            person.setEmail("234234@qq.comhellohellohellohellohellohellohellohello你好你好hellohellohellohellohellohellohellohello你好你好");
            person.setId(2);
            person.setName("hellohellohellohellohellohellohellohello你好你好hellohellohellohellohellohellohellohello你好你好");

            String json = JSON.toJSONString(person, false);
            byte[] bytes = json.getBytes(Charset.forName("UTF-8"));

            String json2 = new String(bytes, Charset.forName("UTF-8"));
            person3 = JSON.parseObject(json2, Person.class);

        }
        System.out.println(person3.getName() + ", " + person3.getEmail());
        System.out.println("fastjson cast time:" + helper.cast());
    }
}
