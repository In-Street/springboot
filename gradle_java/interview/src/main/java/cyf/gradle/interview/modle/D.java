package cyf.gradle.interview.modle;

/**
 * @author Cheng Yufei
 * @create 2019-02-17 15:40
 **/
public class D extends C implements E{

    private String name;


    public String getDName() {
        return "D";
    }

    public D() {
    }

    public D(String name) {
        this.name = name;
    }

    @Override
    public String getEName() {
        return null;
    }
}
