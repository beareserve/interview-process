package jvm;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XyClassLoader extends ClassLoader{
    private String classPath;

    public XyClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name); //找到已加载的类
            if (c == null) {
                long t0 = System.nanoTime();
                //双亲委派的逻辑
//                try {
//                    if (parent != null) {
//                        c = parent.loadClass(name, false);
//                    } else {
//                        c = findBootstrapClassOrNull(name);
//                    }
//                } catch (ClassNotFoundException e) {
//                    // ClassNotFoundException thrown if class not found
//                    // from the non-null parent class loader
//                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    if (name.startsWith("jvm.")) {
                        c = findClass(name);
                    } else {
                        c = this.getParent().loadClass(name);
                    }

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    private byte[] loadByte(String name) throws IOException {
        name = name.replaceAll("\\.", "/");
        FileInputStream fis = new FileInputStream(classPath + "/" + name + ".class");
        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        XyClassLoader xyClassLoader = new XyClassLoader("C:\\Users\\run\\IdeaProjects\\interview-process\\target\\classes");
//        Class clazz = xyClassLoader.loadClass("jvm.JvmClassLoadTest");
        XyClassLoader xyClassLoader = new XyClassLoader("d:\\test");
        Class clazz = xyClassLoader.loadClass("jvm.UserReal");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("sout", null);
        method.invoke(obj, null);
        System.out.println("UserReal类的加载器是：" + clazz.getClassLoader().getClass().getName());

        Class clazz1 = xyClassLoader.loadClass("jvm.JvmClassLoadTest");
        Object obj1 = clazz1.newInstance();
        Method method1 = clazz1.getDeclaredMethod("sout", null);
        method1.invoke(obj1, null);
        System.out.println("JvmClassLoadTest类的加载器是：" + clazz1.getClassLoader().getClass().getName());
    }
}
