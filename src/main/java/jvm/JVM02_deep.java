package jvm;

public class JVM02_deep {

    private int id;

    public int getId() {
        return id;
    }

    public JVM02_deep(int id) {
        this.id = id;
    }

    public JVM02_deep setId(int id) {
        this.id = id;
        return this;
    }


    public static void main(String[] args) {
        int a = 1;
        int b = 1;
        int c = a + b;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println(this.getId() + "即将被回收，思考怎么自救吧");
    }
}
