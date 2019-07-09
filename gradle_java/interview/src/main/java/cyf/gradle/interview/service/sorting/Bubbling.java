package cyf.gradle.interview.service.sorting;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * 冒泡排序
 *
 * @author Cheng Yufei
 * @create 2019-07-03 17:35
 **/
public class Bubbling {

    public static void main(String[] args) {
        ArrayList<Integer> list = Lists.newArrayList(25, 20, 10, 24, 11, 5, 3, 2, 4, 1);
        //10
        int size = list.size();
        Integer mid;

        for (int i = 0; i <= size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {

                if (list.get(j) > list.get(j + 1)) {
                    mid = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, mid);
                }
            }
        }
        System.out.println(list);

        //list.sort(Comparator.comparing(i->i));
        //reversed时注意类型转换
        //List<Integer> result = list.stream().sorted(Comparator.comparingInt(i->(Integer) i).reversed()).collect(Collectors.toList());
        //List<Integer> result = list.stream().sorted(Comparator.comparing(i->(Integer)i).reversed()).collect(Collectors.toList());
        //System.out.println(list);
    }

    /**
     *  将所有元素两两比较，将最大的放在最后；
     *  将剩余元素两两比较，将最大的放后面，重复直到剩下最后一个元素；
     *
     *  每经过一轮比较会有一个最大数产生，所以一共循环size-1 次。
     *
     *  实现：
     *      外层循环限制循环次数，里层循环比较并移动位置；
     *      每次循环都会将最大数放在最后，所以下次循环从0位置开始比较到上次最大数位置之前即可：size-1-i ;
     *
     *      https://mmbiz.qpic.cn/mmbiz_gif/QCu849YTaIOOdfiakqsTRHKk9icjqQZJYuffv5BticjiaK3BNNtdH6dRFglibdwgA9w2oR6QZTadJeZHdOsicqyjasPg/640?wxfrom=5&wx_lazy=1
     */
}
