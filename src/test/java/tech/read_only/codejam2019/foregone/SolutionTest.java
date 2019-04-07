package tech.read_only.codejam2019.foregone;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static ByteArrayOutputStream OUT = new ByteArrayOutputStream();

    @BeforeClass
    public static void setUpClass() {
        System.setOut(new PrintStream(OUT));
    }

    @After
    public void tearDown() {
        OUT.reset();
    }

    private static void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void test2() {
        setInput("1\n2");

        Solution.main(null);

        assertEquals("1 1\n", OUT.toString());
    }

    @Test
    public void test3() {
        setInput("1\n3");

        Solution.main(null);

        assertEquals("2 1\n", OUT.toString());
    }

    @Test
    public void test4() {
        setInput("1\n4");

        Solution.main(null);

        assertEquals("2 2\n", OUT.toString());
    }

    @Test
    public void test128() {
        setInput("1\n128");

        Solution.main(null);

        assertEquals("65 63\n", OUT.toString());
    }

    @Test
    public void test333() {
        setInput("1\n333");

        Solution.main(null);

        assertEquals("167 166\n", OUT.toString());
    }

    @Test
    public void test801() {
        setInput("1\n801");

        Solution.main(null);

        assertEquals("500 301\n", OUT.toString());
    }

    @Test
    public void test880() {
        setInput("1\n880");

        Solution.main(null);

        assertEquals("500 380\n", OUT.toString());
    }

    @Test
    public void test909() {
        setInput("1\n909");

        Solution.main(null);

        assertEquals("510 399\n", OUT.toString());
    }

    @Test
    public void test1089() {
        setInput("1\n1090");

        Solution.main(null);

        assertEquals("551 539\n", OUT.toString());
    }

    @Test
    public void test1088() {
        setInput("1\n1088");

        Solution.main(null);

        assertEquals("550 538\n", OUT.toString());
    }

    @Test
    public void test87346598() {
        setInput("1\n87346598");

        Solution.main(null);

        assertEquals("50006599 37339999\n", OUT.toString());
    }

    @Test
    public void test87346598763245099823476345_18763452348762349872435_520987345763245002000_909009000090000009() {
        setInput("4\n" +
                 "87346598763245099823476345\n" +
                 "18763452348762349872435\n" +
                 "520987345763245002000\n" +
                 "909009000090000009");

        Solution.main(null);

        assertEquals("50006598763250000000076350 37339999999995099823399995\n" +
                             "9381726175000009872500 9381726173762339999935\n" +
                             "260587350000005002001 260399995763239999999\n" +
                             "509009000090000010 399999999999999999\n",
                     OUT.toString());
    }
}
