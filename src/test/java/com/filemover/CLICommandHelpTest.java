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
public class CLICommandHelpTest {
    public static CLI cli;
    public static Application app;

    static {
        app = new Application();
        cli = new CLI(app);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"move --help", CLICommandHelpTest.cli.createHelpMove().trim() },
                {"move -h", CLICommandHelpTest.cli.createHelpMove().trim() },
                {"config -h", CLICommandHelpTest.cli.createHelpConfig().trim() },
                {"config --help", CLICommandHelpTest.cli.createHelpConfig().trim() },
                {"show -h", CLICommandHelpTest.cli.createHelpShow().trim() },
                {"show --help", CLICommandHelpTest.cli.createHelpShow().trim() },
                {"remove -h", CLICommandHelpTest.cli.createHelpRemove().trim() },
                {"remove --help", CLICommandHelpTest.cli.createHelpRemove().trim() },
                {"add -h", CLICommandHelpTest.cli.createHelpAdd().trim() },
                {"add --help", CLICommandHelpTest.cli.createHelpAdd().trim() },
                {"quit -h", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"quit --help", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"q -h", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"q --help", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"exit -h", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"exit --help", CLICommandHelpTest.cli.createHelpQuit().trim() },
                {"help", CLICommandHelpTest.cli.createHelpAll().trim() },
        });
    }

    private String input;
    private String expected;
    private PrintStream console = System.out;
    private ByteArrayOutputStream testOutput;


    public CLICommandHelpTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() throws ParseException {

        testOutput = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(testOutput));
            CLICommandHelpTest.cli.execute(input);
        } finally {
            System.setOut(console);
        }
        assertEquals(expected, testOutput.toString().trim());
    }
}


//
// example of parametrized test class

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