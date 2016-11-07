	

    import java.util.Random;
     
     
    public class Quicksort {
     
            private static final int size = 1000000;
            /**
             * @param args
             */
            public static void main(String[] args) {
                    // TODO Auto-generated method stub
                    Random rand = new Random();
                   
                    Quicksort sorter = new Quicksort();
                   
                    int[] randomArray = new int[size];
                    int[] randomArray2;
                   
                   
                   
                    for(int i = 0; i < size; i++)
                    {
                            randomArray[i] = rand.nextInt(size * 2);
                    }
    //              for(int i : randomArray)
    //              {
    //                      System.out.print(i + " ");
    //              }
    //              System.out.println();
                    randomArray2 = randomArray.clone();
                    double time = System.currentTimeMillis();
                    sorter.quickSort(randomArray, 0, size - 1);
                    System.out.println("Time: " + (System.currentTimeMillis() - time));
                    time = System.currentTimeMillis();
                    sorter.selectionSort(randomArray2);
                    System.out.println("Time: " + (System.currentTimeMillis() - time));
    //              for(int i : randomArray)
    //              {
    //                      System.out.print(i + " ");
    //              }
    //              System.out.println();
    //              for(int i : randomArray2)
    //              {
    //                      System.out.print(i + " ");
    //              }
            }
           
            public void quickSort(int[] toSort, int beg, int end) {
                    if(beg >= end) {
                            return;
                    }
                    int wall = beg;
                    int pivot = toSort[end];

                    for(int i = beg; i < end; i++) {
                            if(toSort[i] < pivot) {
                                    int temp = toSort[i];
                                    toSort[i] = toSort[wall];
                                    toSort[wall++] = temp;
                            }
                    }
                    toSort[end] = toSort[wall];
                    toSort[wall++] = pivot;

                    quickSort(toSort, beg, wall - 2);
                    quickSort(toSort, wall, end);
            }
           
            public void selectionSort(int[] toSort) {
                    int l = toSort.length;
                    for(int i = 0; i < l; i++) {
                            int index = i;
                            for(int j = i; j < l; j++) {
                                    if(toSort[j] < toSort[index]) {
                                            index = j;
                                    }
                            }
                            int temp = toSort[index];
                            toSort[index] = toSort[i];
                            toSort[i] = temp;
                    }
                   
            }
     
    }

