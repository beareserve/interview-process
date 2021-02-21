package jvm;

import com.alibaba.fastjson.JSON;
import sun.misc.Launcher;

import java.net.URL;

public class JvmClassLoadTest {

    public static void main(String[] args) {

        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = appClassLoader.getParent();
        ClassLoader bootstrapLoader = extClassLoader.getParent();

        System.out.println(bootstrapLoader + "\n" + extClassLoader + "\n" + appClassLoader);

        System.out.println("bootstrapLoader 加载以下文件");
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        System.out.println(JSON.toJSONString(urls).replace(",", "\n") + "\n");

        System.out.println("extClassLoader 加载以下文件");
        System.out.println(System.getProperty("java.ext.dirs").replace(";", "\n") + "\n");

        System.out.println("appClassLoader 加载以下文件");
        System.out.println(System.getProperty("java.class.path").replace(";", "\n"));

    }

    public void sout() {
        System.out.println("这是我的方法");
    }
}
