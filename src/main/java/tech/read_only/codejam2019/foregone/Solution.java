package tech.read_only.codejam2019.foregone;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {
    public static void main(final String[] args) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            final int numCases = Integer.valueOf(reader.readLine());
            for (int i = 1; i <= numCases; i++) {
                System.out.print("Case #" + i + ": ");
                solveAndPrint(reader.readLine().trim());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void solveAndPrint(final String numStr) {
        final FourlessNumber check2 = new FourlessNumber(numStr.length());
        final boolean carry = initializeHalfNumber(numStr, check2);

        final FourlessNumber check1 = new FourlessNumber(check2);
        if (carry) {
            check1.increment();
        }

        solve(check1, check2);
        System.out.println(check1.toString() + ' ' + check2.toString());
    }

    private static void solve(final FourlessNumber check1, final FourlessNumber check2) {
        // do one up front, to guarantee we've checked both at least once before exiting the loop
        FourlessNumber difference = check1.nextNumber(FourlessNumber.NAN);
        for (int i = 0; ; i = (i+1)%2) {
            if (i == 0) {
                difference = check2.prevNumber(difference);
            } else {
                difference = check1.nextNumber(difference);
            }

            if (difference.isZero()) {
                break;
            }
        }
    }

    private static boolean initializeHalfNumber(final String numStr, final FourlessNumber check1) {
        final StringBuilder sb = new StringBuilder();
        boolean carry = false;
        for (int i = 0; i < numStr.length(); i++) {
            final int val = Integer.valueOf(numStr.substring(i, i + 1));

            if (carry) {
                sb.append(val/2 + 5);
            }
            else {
                sb.append(val/2);
            }

            carry = val % 2 == 1;
        }

        check1.init(sb.toString());

        return carry;
    }
}
