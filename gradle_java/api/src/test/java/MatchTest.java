import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author Cheng Yufei
 * @create 2018-07-11 11:58
 **/
public class MatchTest {

    /**
     * 正则匹配：
     *          ^ :以什么开头 ^[a-z] 以小写字母开头
     *          $ :以什么结尾  [a-z]$ 以小写字母结尾
     *              ^[a-z0-9]$ 精准匹配字符串，包含小写字母和数字一位： q true  ;  4 true;   44 false ; q3 false;
     *              ^[a-z][0-9]$ 精准匹配2位字符串，以小写字母开头，以数字结尾： q false; 4 false; q4 true ; 4q false;
     *              ^[^a-z][0-9]$ 精准匹配2位字符串，以非小写字母开头，以数字结尾，^ 在[] 内表示非的意思: 12 true ; #3 true ; e3 false;
     *
     *          {x} :前面的字符或字符簇只出现 x 次
     *          {x,} :前面的内容出现 x 次或更多次
     *          {x,y} :前面的内容至少出现x次，最多出现y次
     *          * 与{0,} 效果相同
     *          + 与{1,} 效果相同
     *          ? 与 {0,1} 效果想通过
     */


    @Test
    public void Pattern() {
        //至少3位字符串：字母开头至少1位，以2位数字结尾
        String str = "qw34"; //true
        boolean matches = Pattern.matches("^[a-z]+[0-9]{2}$", str);
        System.out.println(matches);

        //6开头6结尾中间4位：
        String str_1 = "6r12r6";
        boolean matches_1 = Pattern.matches("^6[a-z0-9]{4}6$", str_1);
        System.out.println(matches_1);

        //1位字母或数字
        String str_2 = "q";
        boolean matches_2 = Pattern.matches("^[a-z0-9]$", str_2);
        System.out.println(matches_2);

        //2位字母开头数字结尾
        String str_3 = "w24"; //fasle
        boolean matches_3 = Pattern.matches("^[a-z][0-9]$", str_3);
        System.out.println(matches_3);

        //2 位非字母开头数字结尾
        String str_4 = "#3";
        boolean matches_4 = Pattern.matches("^[^a-z][0-9]$", str_4);
        System.out.println(matches_4);

    }
}
