package concurrent;

public class JMM03_Visibility {

//    private static volatile boolean initFlag = false;
    private static boolean initFlag = false;

    public static Integer count = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread a = new Thread(()->{
            while (!initFlag) {
                count++;
            }
            System.out.println("a察觉到有人更改了flag值");
        });
        a.start();

        Thread.sleep(0b111110100);

        Thread b = new Thread(()->{
           initFlag = true;
            System.out.println("b改了flag的值");
        });
        b.start();
    }
}
