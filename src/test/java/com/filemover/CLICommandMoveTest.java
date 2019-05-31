package com.filemover;

import com.filemover.Model.Application;
import com.filemover.View.CLI;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CLICommandMoveTest {
    public static CLI cli;
    public static Application app;
//    public static String expectedHelpMenu;
    static {
        app = new Application();
        cli = new CLI(app);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
//                { "0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
                // have to add \n becouse in exectue help is shown by sout which adds additional line
                {"help", CLICommandMoveTest.cli.createHelpAll() + "\n" },
//                {"move --help", CLICommandMoveTest.cli.createHelp }
//                {"0", "2"},
//                {"1", "0"},
//                {"0", "0"},

        });
    }

    private String input;
    private String expected;
    private PrintStream console = System.out;
    private ByteArrayOutputStream testOutput;


    public CLICommandMoveTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() throws ParseException {

        testOutput = new ByteArrayOutputStream();
//            CLICommandMoveTest.cli.execute(input);

        try {
            System.setOut(new PrintStream(testOutput));
            CLICommandMoveTest.cli.execute(input);

//            System.out.println(CLICommandMoveTest.cli.createHelpAll());

//            System.out.println("abc");
//            System.out.println("qwe");
        } finally {
            System.setOut(console);
        }
//        System.out.println(testOutput);
//        System.out.println(expected);

        assertEquals(expected, testOutput.toString());
    }


}

//import static org.junit.Assert.assertEquals;
//
//        import java.util.Arrays;
//        import java.util.Collection;
//
//        import org.junit.Test;
//        import org.junit.runner.RunWith;
//        import org.junit.runners.Parameterized;
//        import org.junit.runners.Parameterized.Parameters;

//@RunWith(Parameterized.class)
//public class FibonacciTest {
//    @Parameters
//    public static Collection<Object[]> data() {
//        return Arrays.asList(new Object[][] {
//                { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 }
//        });
//    }
//
//    private int fInput;
//
//    private int fExpected;
//
//    public FibonacciTest(int input, int expected) {
//        this.fInput = input;
//        this.fExpected = expected;
//    }
//
//    @Test
//    public void test() {
//        assertEquals(fExpected, Fibonacci.compute(fInput));
//    }
//}