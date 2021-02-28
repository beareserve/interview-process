package jvm;

import com.google.common.collect.Lists;

import java.util.List;

public class JVM03_GcFinalizeTest {

    public static void main(String[] args) {

        List<JVM02_deep> list = Lists.newArrayList();
        int i = 0,j = 0;
        while (true) {
            list.add(new JVM02_deep(i++));
            new JVM02_deep(j--);
        }
    }
}

