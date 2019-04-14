package tech.read_only.codejam2019.foregone;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static tech.read_only.codejam2019.foregone.FourlessNumber.NAN;

public class FourlessNumberTests {
    @Test
    public void zeroEqualsZero() {
        final FourlessNumber zero1 = new FourlessNumber(0);
        final FourlessNumber zero2 = new FourlessNumber(0);

        assertEquals(zero1, zero2);
        assertEquals(NAN, zero1);
        assertEquals(zero1.hashCode(), zero2.hashCode());
        assertEquals(NAN.hashCode(), zero2.hashCode());
        assertEquals(0, zero1.compareTo(zero2));
    }

    @Test
    public void nonZero() {
        final long num1 = RandomUtils.nextLong();
        final String numStr1 = String.valueOf(num1);
        final FourlessNumber fnum1 = new FourlessNumber(String.valueOf(Long.MAX_VALUE).length());
        fnum1.init(numStr1);
        final FourlessNumber fnum2 = new FourlessNumber(fnum1);

        assertEquals("Should be equal to itself: " + num1,
                     fnum1, fnum2);
        assertEquals("Hashcode should be equal:" + num1,
                     fnum1.hashCode(), fnum2.hashCode());
        assertEquals("CompareTo should give 0:" + num1,
                     0, fnum1.compareTo(fnum2));

        long num2 = RandomUtils.nextLong();
        if (num1 == num2) {
            num2 = num2/2 + Long.MAX_VALUE/2;
        }
        final String numStr2 = String.valueOf(num2);
        fnum2.init(numStr2);

        assertNotEquals("Should not be equal: " + num1 + ", " + num2,
                        fnum1, fnum2);
        assertEquals("CompareTo should have a consistent sign: " + num1 + ", " + num2,
                     Integer.signum(Long.compare(num1, num2)), fnum1.compareTo(fnum2));
    }
}
