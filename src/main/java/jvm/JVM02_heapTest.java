package jvm;

import com.google.common.collect.Lists;

import java.util.List;

public class JVM02_heapTest {

    public static void main(String[] args) throws InterruptedException {
        List<JVM02_deep> list = Lists.newArrayList();
        while (true) {
            list.add(new JVM02_deep());
//            Thread.sleep(1);
        }
    }
}
