import javax.xml.crypto.Data;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class Algorithms{

    public static ArrayList<String> ReadData(String pathname) {
        ArrayList<String> strlist = new ArrayList<String>();
        try {

            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            int j = 0;
            String line = "";
            while ((line = br.readLine()) != null) {
                strlist.add(line);
            }
            return strlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strlist;
    }

    public static ArrayList<ArrayList<Integer> > DataWash(ArrayList<String> Datalist) {
        ArrayList<ArrayList<Integer> > AIS = new ArrayList<ArrayList<Integer> >();
        ArrayList<Integer> IS = new ArrayList<Integer>();
        for (int i = 0; i < Datalist.size(); i++) {
            String Tstr = Datalist.get(i);
            if (Tstr.equals("A") == false) {
                IS.add(Integer.parseInt(Tstr));
            }
            if (Tstr.equals("A")) {
                ArrayList<Integer> elemAIS = new ArrayList<Integer>(IS);
                AIS.add(elemAIS);
                IS.clear();
            }
        }
        return AIS;
    }




    /**
     * Check if the last 3 digits (Ar[n-3], Ar[n-2], Ar[n-1]) create a peak and return true or false - (rules 1 and 3) 
     * 
     * @param Ar    the array of input sequence
     * @param n     the number of array indices that we consider (indices 0 up to (n-1))
     * @return      whether Ar[n-3], Ar[n-1], Ar[n-1] create a peak or not
     */
    public static boolean isPeak(int[] Ar, int n){
        if(n>=3 && (Ar[n-1] < Ar[n-2]) && (Ar[n-2] > Ar[n-3]))
            return true;
        return false;
    }


    /**
     * Check what is the max value in the first column of an array with the first index up to (n-3) and return that value.
     * While doing above, consider the rules 2 and 4 (slope rule)
     * 
     * @param dArray    two-dimensional dynamic array that contains:
     *                 [x][0] <- integer of max possible peaks in a row or 0 if last 3 numbers don't create a peak 
     *                 [x][1] <- west slope value 
     *                 [x][2] <- peak value
     *                 [x][3] <- east slope value 
     *                 so dArray[y][1], dArray[y][2] and dArray[y][3] contains indices y,y+1 and y+2 of a sequence input array in LongestRange 
     * 
     * @param n        the amount of rows that we consider (-3 as we don't consider new peak and other peaks that are joint with it) - rule 2
     * @return         the largest possible amount of peaks creating peak sequence up to n-3 index of an array excluded
     */
    public static int maxRangeValue(int[][] dArray, int n){
        int max = 0;
        for(int i=0; i<n-5; i++){
            if(dArray[i][0] > max && dArray[i][2] >= dArray[n-3][1] && dArray[i][3] <= dArray[n-3][1]){
                max = dArray[i][0];
            }
        }
        return max;
    }

        /* DESCRIPTION OF A RECURSIVE SOLUTION 

        if(n<3) 
            return 0
        if(n>=3)
            if last 3 digits create a peak and don't violate the slope rule (rule 4), return max((1 + LongestRange(A, (n-3))), max(LongestRange(A, (n-1)), LongestRange(A, (n-2)))) 
            that is a max of every other "sub n" solution

            else return LongestRange(A, (n-1))

        --------------------------------------------------------
        

        DESCRIPTION OF THE MAIN IDEAS OF THE SOLUTION

        Let us first define the dynamic programming (DP) array (dynamicArray[x][4]) s.t. the first column contains 
        the value of the longest range of peaks in x+3 first indices of an input sequence array.
        The second column contains the value at the x index of the input sequence array, the third column containsthe value at x+1 index and the
        fourth column containsthe value at the x+2 index. These are meant to be west slope, peak and east slope respectively.

        DP array is first initialised as follows:
        If values at indices i+1, i+2 and i+3 form a peak (that is i+1 < i+2 > i+3), then 
        dynamicArray[x][0] is set to 1
        dynamicArray[x][1], [x][2] and [x][3] are filled with values at index x+1, x+2 and x+3 in an input sequence array.
        Otherwise the result (dynamicArray[x][0]) is set to 0 and the rest of values are set to -1.
        This happens as we are not interested in numbers that don't form a peak.

        DP can be solved recursively as follows:
        If the value in the first column in dynamicArray is equal to 1, then the maxRangeValue method is called.
        It returns the value of the longest range containing of previous indices that do not colide with the new peak (fourth rule)
        That is dynamicArray[y][2] >= dynamicArray[x][1] >= dynamicArray[y][3]
        Given an array T[1,...,n][0]
        then M = max_{x,y where x > y+2 : T[y][2] >= T[x][1] >= T[y][3] holds} [ T[y][0] ]
        The T[x][0] is set to 1 + M. 

        The main ideas of a sequential implementation of the above recursive solution
        that shows how to fill in the DP array is as follows ...
        1. Check if the last 3 numbers form a peak
        2. If they don't, then omit them and move to the next group of 3 last numbers (an index further)
           If they do, then find the max result value at previous indices (previous max values are already found), 
           so that the peaks are disjoint, and add this value to the result of a current index.
        3. Return the max result of a dynamicArray, that is the Result = max_{k} [ T[k][0] ] 

        */

        /* TIME COMPLEXITY ANALYSIS

        O(1)                               for the isPeak method
        O(n-5) -> O(n)                     for the maxRangeValue method
        O(n-2)                             to initialise the dynamicArray
        O((n-2)*(maxRangeValue)) -> O(n^2) in the worst case scenario to change the value of every single result in the dynamicArray[x][0]
        O(n-2)                             to loop through the dynamicArray and find the maximum value of the result (dynamicArray[x][0])

        Hence, the total worst case running time is O((n-2) + (n^2) + (n-2)) -> O(n^2)


        Summarising, this argument shows that the worst case running time of this whole implementation is
        O(n^2).
        */

    public static int LongestRange(int[] A, int n){

        // first value is a result, second value is a west slope, third value is a peak, fourth value is an east slope
        // dynamicArray[x][y] means that values in A from index 0 to index 'x'+2 are considered 
        int[][] dynamicArray = new int[n-2][4];
        
        // fill in the array with the: first value meaning if the last 3 numbers form a peak (1 = yes, 0 = no) 
        // second, third and fourth values with the values of the peak or -1 if one doesn't occur
        // Do not consider first 2 numbers alone as they never form a peak without a 3rd number
        for(int i=0; i<n-2; i++){
            if(isPeak(A, i+3)){
                dynamicArray[i][0] = 1;
                dynamicArray[i][1] = A[i];
                dynamicArray[i][2] = A[i+1];
                dynamicArray[i][3] = A[i+2];
            }
            else{
                dynamicArray[i][0] = 0;
                dynamicArray[i][1] = -1;
                dynamicArray[i][2] = -1;
                dynamicArray[i][3] = -1;
            }
        }

        // Don't consider non peak positions as they don't change the maximum value of peak sequence
        for(int i=0; i<n-2; i++){
            if(dynamicArray[i][0] == 1){
                dynamicArray[i][0] = 1 + maxRangeValue(dynamicArray, i+3); // i+3 because for instance for i = 0, first 0+3 numbers are considered 
            }
        }

        // find the longest range of peaks in a dynamic array and return it
        int longestPeakRange = 0;
        for(int i=0; i<n-2;i++){
            if(dynamicArray[i][0] > longestPeakRange)
                longestPeakRange = dynamicArray[i][0];
        }
        return longestPeakRange;
    }




    public static int Computation(ArrayList<Integer> Instance, int opt){
        // opt=1 here means option 1 as in -opt1, and opt=2 means option 2 as in -opt2
        int NGounp = 0;
        int size = 0;
        int Correct = 0;
        size = Instance.size();

        int [] A = new int[size-opt];
        // NGounp = Integer.parseInt((String)Instance.get(0));
        NGounp = Instance.get(0); // NOTE: NGounp = 0 always, as it is NOT used for any purpose
                                           // It is just the first "0" in the first line before instance starts.
        for (int i = opt; i< size;i++ ){
            A[i-opt] = Instance.get(i);
        }
        int Size =A.length;
        if (NGounp >Size ){
            return (-1);
        }
        else {
            //Size
            int R = LongestRange(A,Size);
            return(R);
        }
    }

    public static String Test;


    public static void main(String[] args) {
        if (args.length == 0) {
            String msg = "Rerun with flag: " +
            "\n\t -opt1 to get input from dataOne.txt " +
            "\n\t -opt2 to check results in dataTwo.txt";
            System.out.println(msg);
            return;
        }
        Test = args[0];
        int opt = 2;
        String pathname = "dataTwo.txt";
        if (Test.equals("-opt1")) {
            opt = 1;
            pathname = "dataOne.txt";
        }


        ArrayList<String> Datalist = new ArrayList<String>();
        Datalist = ReadData(pathname);
        ArrayList<ArrayList<Integer> > AIS = DataWash(Datalist);

        int Nins = AIS.size();
        int NGounp = 0;
        int size = 0;
        if (Test.equals("-opt1")) {
            for (int t = 0; t < Nins; t++) {
                int Correct = 0;
                int Result = 0;
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
            }
        }
        else {
            int wrong_no = 0;
            int Correct = 0;
            int Result = 0;
            ArrayList<Integer> Wrong = new ArrayList<Integer>();
            for (int t = 0; t < Nins; t++) {
                ArrayList<Integer> Instance = AIS.get(t);
                Result = Computation(Instance, opt);
                System.out.println(Result);
                Correct = Instance.get(1);
                if (Correct != Result) {
                    Wrong.add(t+1);
                    wrong_no=wrong_no+1;
                }
            }
            if (Wrong.size() > 0) {System.out.println("Index of wrong instance(s):");}
            for (int j = 0; j < Wrong.size(); j++) {
                System.out.print(Wrong.get(j));
                System.out.print(",");

                /*ArrayList Instance = (ArrayList)Wrong.get(j);
                for (int k = 0; k < Instance.size(); k++){
                    System.out.println(Instance.get(k));
                }*/
            }
            System.out.println("");
            System.out.println("Percentage of correct answers:");
            System.out.println(((double)(Nins-wrong_no) / (double)Nins)*100);

        }

    }
}
