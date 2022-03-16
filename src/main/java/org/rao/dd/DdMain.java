package org.rao.dd;

import java.util.Arrays;

/**
 * @author Rao
 * @Date 2021/12/14
 **/
public class DdMain {

    public static void main(String[] args) {

        Solution solution = new Solution();

        int[] arr = {3,4,6,6,3};
        long game = solution.arrayGame(arr);
        System.out.println(game);

    }


    public static class Solution {
        /**
         * @param arr: the array
         * @return: determine the number of moves to make all elements equals
         */
        // 521  532 543 554 655 666
        // 1234  找到规律了 2344 3454 4555 5665 6766 7777
        public long arrayGame(int[] arr) {
            // write your code here
            int step = 0;

            // 找到最大值 其他每一个都加一 出现相同时，选择下一个，
            while(true){

                int count = 1;
                for (int i = 1; i < arr.length; i++) {
                    if(arr[0] == arr[i]){
                        count ++;
                    }
                }
                if( count == arr.length ){
                    break;
                }

                int max = arr[0];
                int maxIndex = 0;
                for (int i = 1; i < arr.length; i++) {
                    if(arr[i] >= max){
                        max = arr[i];
                        arr[maxIndex] +=1;
                        maxIndex = i;
                    }else{
                        arr[i] +=1;
                    }
                }
                step ++;

            }
            return step;
        }
    }

}

