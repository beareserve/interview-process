package concurrent;

import com.alibaba.fastjson.JSON;

public class JMM01_Base {

    private static final int RUNS = 100;
    public static final int DIMENSION_1 = 1024*1024;
    public static final int DIMENSION_2 = 6;
    public static long[][] longs;

    public static void main(String[] args) {
        longs = new long[DIMENSION_1][DIMENSION_2];
        for (int i = 0; i < DIMENSION_1; i++) {
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 1L;
            }
        }

//        System.out.println("Array初始化完毕:" + JSON.toJSONString(longs));

        long sum = 0L;
        long start = System.currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int i = 0; i < DIMENSION_1; i++) {
                for (int j = 0; j < DIMENSION_2; j++) {
                    sum += longs[i][j];
                }
            }
        }

        System.out.println("spend time1:" + (System.currentTimeMillis() - start));
        System.out.println("sum1:" + sum);

        sum = 0L;
        start = System.currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int i = 0; i < DIMENSION_2; i++) {
                for (int j = 0; j < DIMENSION_1; j++) {
                    sum += longs[j][i];
                }
            }
        }
        System.out.println("spend time2:" + (System.currentTimeMillis() - start));
        System.out.println("sum2:" + sum);
    }
}
