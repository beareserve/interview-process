package concurrent;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class XyTest {

    static Object generate() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", new Integer(1));
        map.put("b", "b");
        map.put("c", new Date());

        for (int i = 0; i < 10; i++) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
        return map;
    }

    static void print(String message, String type) {
        System.out.println("==========================================================" + type + "\n" + message);
    }

    public static void main(String[] args) throws InterruptedException {
//        Object obj = generate();
        //-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=2
        Object obj1 = new Object();
        print(ClassLayout.parseInstance(obj1).toPrintable(), "默认偏向锁延迟系统加载5s后启动，对象在这之前创建，无锁：001");

        sleep(6000L);
        Object obj = new Object();
        print(ClassLayout.parseInstance(obj).toPrintable(), "系统延迟6s后启动，这之后创建的对象，自动匿名偏向锁：101");

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    print(ClassLayout.parseInstance(obj).toPrintable(), "线程1拿到锁，依然偏向锁：101");
                }
            }
        }).start();

//        sleep(1);
        print(ClassLayout.parseInstance(obj).toPrintable(), "回到主线程，依然偏向锁：101");
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    print(ClassLayout.parseInstance(obj).toPrintable(), "线程2再次尝试加锁后，偏向升级为轻量锁：000");
                }
            }
        }).start();

        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (obj) {
                        try {
                            sleep(500l);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        sleep(600L);
        print(ClassLayout.parseInstance(obj).toPrintable(), "睡眠0.6s后，因为每个线程会睡眠0.5s,这样线程就会开始争抢，重量锁：010");

//        //查看对象内部信息
//        print(ClassLayout.parseInstance(obj).toPrintable());
//
//        //查看对象外部信息
//        print(GraphLayout.parseInstance(obj).toPrintable());
//
//        //获取对象总大小
//        print("size : " + GraphLayout.parseInstance(obj).totalSize());
    }

}
