package com.example.springboot1;

import com.example.springboot1.async.Task;
import com.example.springboot1.controller.HelloController;
import com.example.springboot1.entity.BlogProperties;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * spring-boot1 .
 *
 * @description: .
 * @author: Chenglin Zhu .
 * @date: 19-7-9 .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AnyncTaskTests {

	@Autowired
	private Task t;

	@Test
	public void test() throws Exception {
		t.doTaskOne();
		t.doTaskTwo();
		t.doTaskThree();
		Thread.currentThread().join();
	}


	@Test
	public void test1() throws Exception {
		long start = System.currentTimeMillis();
		Future<String> task1 = t.doTaskOne();
		Future<String> task2 = t.doTaskTwo();
		Future<String> task3 = t.doTaskThree();
		while (!task1.isDone() || !task2.isDone() || !task3.isDone()) {
			Thread.sleep(10000);
		}

		long end = System.currentTimeMillis();
		System.out.println("任务完成，耗时：" + (end - start) + "毫秒");

	}

	@Test
	@SneakyThrows
	public void test2() {

		for (int i = 0; i < 10000; i++) {
			t.doTaskOne();
			t.doTaskTwo();
			t.doTaskThree();

			if (i == 9999) {
				System.exit(0);
			}
		}
	}

	@Test
	public void testFuture() throws Exception {
		Future<String> futureResult = t.run();
		String result = futureResult.get(5, TimeUnit.SECONDS);
		// 获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回
		System.out.println(result);
	}
}
