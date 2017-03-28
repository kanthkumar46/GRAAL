package com.graal;

import com.graal.graphs.PDGraph;
import com.junitsupport.TestSetup;
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

        String javaCode1 = "class Test{public static void main(String args[]){ int i=0; System.out.println(i);} public void nothing() {String st = 's';System.out.println(st);}}";
        String javaCode2 = "class Test{public static void main(String args[]){ int i=0; System.out.println(i);} public void nothing() {String st = 's';System.out.println(st);}}";


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
}