package cyf.gradle.interview.service.sorting;

import java.util.Arrays;

/**
 * 插入排序
 *
 * @author Cheng Yufei
 * @create 2019-07-05 17:25
 **/
public class InsertSort {
    public static void main(String[] args) {
        /**
         * 从第一个元素开始，该元素可以认为已经被排序；
         * 取出下一个元素，在已经排序的元素序列中从后向前扫描；
         * 如果该元素（已排序）大于新元素，将该元素移到下一位置；
         * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置；
         * 将新元素插入到该位置后；
         * 重复步骤2~5。
         *
         *  https://mmbiz.qpic.cn/mmbiz_gif/QCu849YTaIOOdfiakqsTRHKk9icjqQZJYusIFPUq7PlJn7maGNCmlhzTnLCkRcNjulAZk34Elic3oeVka2u4icXWDA/640?wxfrom=5&wx_lazy=1
         */

        int[] array = {25, 20, 10, 24, 11, 5, 3, 2, 4, 1, 0, 26, 6};

        int length = array.length;

        for (int i = 0; i <= length - 1; i++) {
            //要插入的新元素
            int insertNum = array[i];

            //已排序的最后一个元素位置
            int j = i - 1;
            while (j >= 0 && array[j] > insertNum) {
                //从后向前扫描已排序的元素，若比新元素大，则向后移一位
                array[j + 1] = array[j];
                j--;
            }

            //新元素插入
            array[j + 1] = insertNum;

        }
        System.out.println(Arrays.toString(array));
    }
}
