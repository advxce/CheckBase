package task1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Task1 {

    public static void main(String[] args) {
        int[] arr = {2, 10, 5, 7, 9, 3, 5, 6, 1};
/**
 *  1,2,3,5,5,6,7,9,10
 *  2,10,5,7,9,3,5,6,1
 *  sup = 9/2 = 5 => arr[5] = 9
 *  elements < 9 => {2,5,7,3,5,6,1 }
 *  massLEft = {2,10,5,7}
 *  massRight = {3,5,6,1}
 *  for and compare value between mass
 *  if(massLeft[0]> supElem)
 *  swap elem into massRight
 */
//        System.out.println(Arrays.toString(quickSort(arr)));
        quickSortPrototype(arr, 0, arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    static int[] quickSort(int[] arr) {
        if (arr.length <= 1) return arr;
        int supElement = arr[arr.length / 2];
        ArrayList<Integer> leftMass = new ArrayList<>();
        ArrayList<Integer> rightMass = new ArrayList<>();
        ArrayList<Integer> middleMass = new ArrayList<>();

        for(int i = 0; i<= arr.length-1; i++){
            if(arr[i]< supElement){
                leftMass.add(arr[i]);
            } else if(arr[i] > supElement) {
                rightMass.add(arr[i]);
            } else {
                middleMass.add(arr[i]);
            }
        }
        int[] leftArr = leftMass.stream().mapToInt(i -> i).toArray();
        int[] rightArr = rightMass.stream().mapToInt(i -> i).toArray();
        int[] middleArr = middleMass.stream().mapToInt(i->i).toArray();
        int [] l =  quickSort(leftArr);
        int [] r = quickSort(rightArr);

        int[] temp = new int[l.length + middleArr.length];
        System.arraycopy(l, 0, temp, 0, l.length);
        System.arraycopy(middleArr, 0, temp, l.length, middleArr.length);

        int[] result = new int[temp.length + r.length];
        System.arraycopy(temp, 0, result,0, temp.length);
        System.arraycopy(r, 0, result,temp.length, r.length);


        return result;
    }

    static void quickSortPrototype(int []arr, int left, int right){
        if(left>=right) return;
        int i = left;
        int j = right;
        int supportElem = arr[left + (right-left)/ 2];

        while (i<=j){
            while(arr[i]<supportElem) i++;
            while (arr[j]> supportElem) j--;
            if(i<=j){
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        quickSortPrototype(arr, left, j);
        quickSortPrototype(arr, i, right);
    }

}
