package cyf.gradle.interview.service.sorting;

/**
 * 希尔排序
 *
 * @author Cheng Yufei
 * @create 2019-07-05 18:12
 **/
public class ShellSort {

    /**
     * 数组元素的个数设为n，取奇数k=n/2，将下标差值为k的数分为一组，进行插入排序，构成有序序列。
     * 再取k=k/2 ，将下标差值为k的书分为一组，构成有序序列。
     * 重复第二步，直到k=1执行简单插入排序。
     *
     * @param args
     */

    public static void main(String[] args) {

        int[] array = {25, 20, 10, 24, 11, 5, 3, 2, 4, 1, 0, 26, 6};

        int length = array.length;
        int middle = length / 2;
        int insertNum;

        while (middle > 0) {
            for (int i = middle; i < length; i++) {
                 insertNum = array[middle];
                int j = middle - i;
                while (j>=0 && array[j] > insertNum) {
                    array[j + middle] = array[j];
                    j = j - middle;
                }
                array[j+middle] = insertNum;
            }
            middle /= 2;
        }

    }
}
