package edu.graal;

import edu.graal.graphs.PDGraph;
import edu.junitsupport.TestSetup;
import org.junit.Test;

/**
 * Created by KanthKumar on 3/17/17.
 */
public class GraalAlgorithmTest extends TestSetup{
    private GraalAlgorithm graalAlgorithm = new GraalAlgorithm();

    @Test
    public void AlgorithmTest1(){
        String testcode1 = "class Odd {\n" +
                "\tpublic int countOdd(int[] input) {\n" +
                "\t\tint oddCount = 0;\n" +
                "\t\tfor (int i = 0; i < input.length; i++) {\n" +
                "\t\t\tif (input[i] % 2 != 0) {\n" +
                "\t\t\t\toddCount++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn oddCount;\n" +
                "\t}" +
                "}";

        String testcode2 = "class Even {" +
                "\tpublic int countEven(int[] input) {\n" +
                "\t\tint evenCount = 0;\n" +
                "\t\tfor (int i = 0; i < input.length; i++) {\n" +
                "\t\t\tif (input[i] % 2 == 0) {\n" +
                "\t\t\t\tevenCount++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn evenCount;\n" +
                "\t}" +
                "}";

        PDGGenerator pdgGenerator = ImmutablePDGGenerator.of();
        PDGraph original = pdgGenerator.createPDG(testcode1 , 0);
        PDGraph suspect = pdgGenerator.createPDG(testcode2, 0);

        graalAlgorithm.execute(original, suspect);
    }

    @Test
    public void AlgorithmTest2(){
        String testcode1 = "class Test {" +
                "public static void main(String args[]) { " +
                "int i=0; System.out.println(i);" +
                "} " +
                "public void nothing() {" +
                "String st = 's';System.out.println(st);" +
                "}" +
                "}";
        String testcode2 = "class Test {" +
                "public static void main(String args[]) { " +
                "int i=0; System.out.println(i);" +
                "} " +
                "public void nothing() {" +
                "String st = 's';System.out.println(st);" +
                "}" +
                "}";

        PDGGenerator pdgGenerator = ImmutablePDGGenerator.of();
        PDGraph original = pdgGenerator.createPDG(testcode1 , 0);
        PDGraph suspect = pdgGenerator.createPDG(testcode2, 0);

        graalAlgorithm.execute(original, suspect);
    }

    @Test
    public void AlgorithmTest3(){
        String testcode1 = "class Odd {\n" +
                "\tpublic int[] bubbleSort(int[] inputArray) {\n" +
                "\t    for (int indexOne = 0; indexOne < inputArray.length; indexOne++) {\n" +
                "\t    \tint indexTwo = indexOne+1;\n" +
                "\t        while(indexTwo < inputArray.length) {\n" +
                "\t        \tif(inputArray[indexOne] > inputArray[indexTwo]) {\n" +
                "\t            \tint tempVariable = inputArray[indexOne];\n" +
                "\t                inputArray[indexOne] = inputArray[indexTwo];\n" +
                "\t                inputArray[indexTwo] = tempVariable;\n" +
                "\t        \t}\n" +
                "\t            indexTwo++;\n" +
                "\t        }\n" +
                "\t    }\n" +
                "\t    return inputArray;\n" +
                "\t}\n" +
                "\tpublic int countEven(int[] arr) {\n" +
                "\t\tint count = 0;\n" +
                "\t\tint j;\n" +
                "\t\tfor (j = 0; j < arr.length; j++) {\n" +
                "\t\t\tif (arr[j] % 2 == 0) {\n" +
                "\t\t\t\tcount++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn count;\n" +
                "\t}" +
                "}";

        String testcode2 = "class Even {" +
                "\tpublic int countEven(int[] input) {\n" +
                "\t\tint evenCount = 0;\n" +
                "\t\tfor (int i = 0; i < input.length; i++) {\n" +
                "\t\t\tif (input[i] % 2 == 0) {\n" +
                "\t\t\t\tevenCount++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn evenCount;\n" +
                "\t}" +
                "}";

        PDGGenerator pdgGenerator = ImmutablePDGGenerator.of();
        PDGraph original = pdgGenerator.createPDG(testcode1 , 0);
        PDGraph suspect = pdgGenerator.createPDG(testcode2, 0);

        graalAlgorithm.execute(original, suspect);
    }
}