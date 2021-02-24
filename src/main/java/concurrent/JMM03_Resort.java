package concurrent;

public class JMM03_Resort {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;
    public static void main(String[] args) throws InterruptedException {

        int i = 0;
        for (;;) {
            i ++;
            a = 0; b = 0;
            x = 0; y = 0;

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = 1;
                    x = b;
                }
            });

            Thread t2 = new Thread(()->{
               b = 1;
               y = a;
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();

            System.out.println("第" + i + "次，x=" + x + "y=" + y);
            if (x == 0 && y == 0) {
                System.out.println("重排了");
                break;
            }
        }
    }
}
