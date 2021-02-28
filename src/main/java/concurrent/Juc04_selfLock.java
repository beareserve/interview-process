package concurrent;

import sun.misc.Unsafe;
import util.UnsafeUtil;

import java.lang.reflect.Field;

public class Juc04_selfLock {

    private volatile int state = 0;



    public final boolean compareAndSwapSate(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, 0, oldValue, newValue);
    }

    public static final Unsafe unsafe = UnsafeUtil.reflectGetUnsafe();

    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(Juc04_selfLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}






