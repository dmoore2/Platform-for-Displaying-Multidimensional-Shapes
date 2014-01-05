import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
 
import acm.program.*;
import java.util.ArrayList;
import acm.util.*;
 
public class FileGenerator {
     
 
    public FileGenerator(int dimension, String shape, String filename) {
        theShape = shape;
        generateFile(dimension, shape, filename);
    }
     
    private void generateFile(int dimension, String shape, String filename) {
        double[] array = createArray(dimension, 0);
        if (shape.equals("square")) {
            createListOfVectors(array);
        } else if (shape.equals("icosahedron")) {
            createIcosahedronVectors(dimension);
        } else if (shape.equals("bucky")) {
            //createBuckyBall(dimension);
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            for(int i = 0; i < vectors.size(); i++) {
                bw.write(vectors.get(i));
                bw.newLine();
            }
            if (bw != null) {
                bw.flush();
                bw.close();
            }
        }
        catch (Exception e) {};
    }
     
    //creates an array of the given dimension with all entries being the given value
    private double[] createArray(int dimension, int value) {
        double[] array = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            array[i] = value;
        }
        return array;
    }
     
     
    //this is not quite ready :( in a few days I will have the even/odd permutations function working though and then
    //it will be straightforward to create much more complicated shapes including multidimensional buckyballs.
     
    /*private void createBuckyBall(int dim) {
        double[] values = new double[dim];
        double distance = 0;
        if (dim == 3) {
            values = new double[]{1, 1, 1};
            distance = 2;
        } else if (dim == 4) {
            values = new double[]{1, (1+Math.pow(5, (0.5)))/2, 2/(1+Math.pow(5, (0.5))), 0};
            distance = (1+Math.pow(5, (0.5)));
        }
        findPermutations(vectorsDouble, values);
        makeSignVectors(0, new double[dim]);
        findSignCombinations();
        makeLinesUsingDistanceCalc(distance);
        distance = 
        //values = new double[]{2, 1, 1};
        findPermutations(vectorsDouble, values);
        makeSignVectors(0, new double[dim]);
        findSignCombinations();
        makeLinesUsingDistanceCalc(distance);
         
    }*/
     
     
    private void createIcosahedronVectors(int dim) {
        double[] values = new double[dim];
        double distance = 0;
        if (dim == 3) {
            values = new double[]{1, (1+Math.pow(5, (0.5)))/2, 0};
            distance = 2;
        } else if (dim == 4) {
            values = new double[]{1, (1+Math.pow(5, (0.5)))/2, 2/(1+Math.pow(5, (0.5))), 0};
            distance = (1+Math.pow(5, (0.5)));
        }
        findPermutations(vectorsDouble, values);
        makeSignVectors(0, new double[dim]);
        findSignCombinations();
        makeLinesUsingDistanceCalc(distance);
    }
     
    private void makeLinesUsingDistanceCalc(double d) {
        for(int i = 0; i < verticies.size(); i++) {
            for(int j = 0; j < verticies.size(); j++) {
                double distance = 0;
                for(int k = 0; k < verticies.get(0).length; k++) {
                    distance += Math.pow((verticies.get(i)[k]-verticies.get(j)[k]), 2);
                }
                distance = Math.pow(distance, 0.5);
                if (distance == d) {
                    String entry = stringify(verticies.get(i)) + ";" + stringify(verticies.get(j));
                    if (!vectors.contains(entry)) {
                        vectors.add(entry);
                        System.out.println(entry);
                    }
                }
            }
        }
    }
     
    private double calculateDistanceFromCenter() {
        double[] vector = null;
        if (theShape == "square") {
            return 1.0;
        } else if (theShape == "icosahedron") {
            vector = new double[]{1, (1+Math.pow(5, (0.5)))/2, 0};
        }
        double distance = 0;
        for(int i = 0; i < vector.length; i++) {
            distance += Math.pow((vector[i]), 2);
        }
        Math.pow(distance, 0.5);
        return distance;
    }
     
    private void findSignCombinations() {
        for(int i = 0; i < vectorsDouble.size(); i++) {
            for(int j = 0; j < signVectors.size(); j++) {
                double[] newVector = new double[vectorsDouble.get(0).length];
                for(int k = 0; k < vectorsDouble.get(0).length; k++) {
                    if(signVectors.get(j)[k] == 0) {
                        newVector[k] = 0;
                    } else {
                        newVector[k] = vectorsDouble.get(i)[k] * signVectors.get(j)[k];
                    }   
                }
                if (!verticies.contains(newVector)) {
                    verticies.add(newVector);
                    //System.out.println(newVector[0] + " " + newVector[1] + " " + newVector[2] + " " + newVector[3]);  
                }
            }
        }
    }
     
    //creates all combinations of signs i.e. 1 and -1
    private void makeSignVectors(int num, double[] array) {
        if (array.length == num) {
            if (!signVectors.contains(array)) {
                signVectors.add(array);
            }
        } else {
            for (int i = -1; i <= 1; i+=2) {
                double[] newArray = array.clone();
                newArray[num] = i;
                makeSignVectors(num+1, newArray);
            }
        }
    }
     
    //fills a given array list with the various permutations of the values
    private void findCombinations(ArrayList<double[]> arrayList, int index, double[] startArray, double[] endArray) {
        if (index == startArray.length + endArray.length) {
            if (!arrayList.contains(startArray)) {
                arrayList.add(startArray);
                //System.out.println(startArray[0] + " " + startArray[1] + " " + startArray[2]); //for debugging, I couldn't get println to work here so I used System.out.println()
            }
        } else {
            for (int i = 0; i < endArray.length; i++) {
                double[] newStartArray = new double[startArray.length + 1];
                double[] newEndArray = new double[endArray.length - 1];
                //fill new array with start array values
                for (int j = 0; j < startArray.length; j++) {
                    newStartArray[j] = startArray[j];
                }
                newStartArray[newStartArray.length-1] = endArray[i];
                //fill new end array with values
                int idx = 0;
                for (int j = 0; j < endArray.length; j++) {
                    if(endArray[j] != endArray[i]) {
                        newEndArray[idx] = endArray[j];
                        idx++;
                    }
                }
                findCombinations(arrayList, index+1, newStartArray, newEndArray);
            }
        }
    }
     
     
    private void findPermutations(ArrayList<double[]> arrayList, double[] array) {
        for(int i = 0; i < array.length; i++) {
            double[] newArray = new double[array.length];
            newArray[0] = array[i];
            for (int j = 1; j < array.length; j++) {
                int newIndex = i + j;
                if (newIndex >= array.length) {
                    newArray[j] = array[newIndex-array.length];
                } else {
                    newArray[j] = array[newIndex];
                }
            }
            if (!arrayList.contains(newArray)) {
                arrayList.add(newArray);
                //System.out.println(newArray[0] + " " + newArray[1] + " " + newArray[2] + " " + newArray[3]); //for debugging
            }
        }
    }
     
    private void createListOfVectors(double[] array1) {
        for(int i = 0; i < array1.length; i++) {
            if (array1[i] != 1) {
                double[] array2 = array1.clone();
                array2[i] = 1;
                String entry = stringify(array1) + ";" + stringify(array2);
                if (!vectors.contains(entry)) {
                    vectors.add(entry);
                }
                createListOfVectors(array2);
            }
        }
    }
     
    //converts array to string
    private String stringify(double[] array) {
        String result = "";
        for(int i = 0; i < array.length; i++) {
            double entryValue = array[i];
            if (theShape == "square") {
                entryValue -= calculateDistanceFromCenter() / 2; //important for extending program to incorporate more complicated shapes
            }
            result += String.valueOf(entryValue);//Subtracts by 0.5 to center shape
            result += " ";
        }
        return result;
    }
     
    //instance variables
    private String theShape;
    private ArrayList<double[]> verticies = new ArrayList<double[]>();
    private ArrayList<double[]> signVectors = new ArrayList<double[]>();
    private ArrayList<double[]> vectorsDouble = new ArrayList<double[]>();
    private ArrayList<String> vectors = new ArrayList<String>();
}
