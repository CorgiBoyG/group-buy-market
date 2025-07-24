package cn.bugstack.test;

import cn.bugstack.infrastructure.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Test
    public void test() {
        log.info("测试完成");
    }

    @Resource
    private EventPublisher publisher;

    @Value("${spring.rabbitmq.config.producer.topic_team_success.routing_key}")
    private String routingKey;

    @Resource
    private RedissonClient redissonClient;

    /*测试看门狗场景*/
    @Test
    public void test_lock_thread_1() throws InterruptedException {
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec");
//        boolean b = lock.tryLock(3, 0, TimeUnit.SECONDS);
        boolean b = lock.tryLock(3, 3, TimeUnit.SECONDS);

        Thread.sleep(3000);

        log.info("测试结果:{}", b);
    }

    @Test
    public void test_lock_thread_2() throws InterruptedException {
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec");
//        boolean b = lock.tryLock(3, 0, TimeUnit.SECONDS);
        boolean b = lock.tryLock(3, 3, TimeUnit.SECONDS);

        log.info("测试结果:{}", b);
    }

    @Test
    public void test_rabbitmq() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1); //添加后，相当于也是一个application了

        publisher.publish(routingKey, "订单结算：ORD-20231234");
        publisher.publish(routingKey, "订单结算：ORD-20231235");
        publisher.publish(routingKey, "订单结算：ORD-20231236");
        publisher.publish(routingKey, "订单结算：ORD-20231237");
        publisher.publish(routingKey, "订单结算：ORD-20231238");

        // 等待，消息消费。测试后，可主动关闭。相当于没添加这个，发送完了，这里就直接结束了
        countDownLatch.await();
    }

    @Test
    public void test_Supplier() {
        // 创建一个 Supplier 实例，返回一个字符串
        Supplier<String> stringSupplier = () -> "Hello, XFG!";

        // 使用 get() 方法获取 Supplier 提供的值
        String result = stringSupplier.get();

        // 输出结果
        System.out.println(result);

        // 另一个示例，使用 Supplier 提供当前时间
        Supplier<Long> currentTimeSupplier = System::currentTimeMillis;

        // 获取当前时间
        Long currentTime = currentTimeSupplier.get();

        // 输出当前时间
        System.out.println("Current time in milliseconds: " + currentTime);
    }

}
