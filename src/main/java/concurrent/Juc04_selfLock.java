package concurrent;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;
import util.UnsafeUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class Juc04_selfLock {

    private volatile int state = 0;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    private static Juc04_selfLock cas = new Juc04_selfLock();

    public static void main(String[] args) {
        new Thread(new Worker(),"t-0").start();
        new Thread(new Worker(),"t-1").start();
        new Thread(new Worker(),"t-2").start();
        new Thread(new Worker(),"t-3").start();
        new Thread(new Worker(),"t-4").start();
    }

    static class Worker implements Runnable{

        @Override
        public void run() {
            log.info("请求:{}到达预定点,准备开始抢state:)",Thread.currentThread().getName());
            try {
                cyclicBarrier.await();
                if(cas.compareAndSwapState(0,1)){
                    log.info("当前请求:{},抢到锁!",Thread.currentThread().getName());
                }else{
                    log.info("当前请求:{},抢锁失败!",Thread.currentThread().getName());
                }
            } catch (InterruptedException| BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }



    public final boolean compareAndSwapState(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, stateOffset, oldValue, newValue);
    }

    private static final Unsafe unsafe = UnsafeUtil.reflectGetUnsafe();
    private static final long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(Juc04_selfLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            throw new Error();
        }
    }


}






