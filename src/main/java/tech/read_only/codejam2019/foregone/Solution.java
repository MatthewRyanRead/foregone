package tech.read_only.codejam2019.foregone;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {
    public static void main(final String[] args) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            int numNums = Integer.parseInt(reader.readLine());

            for (int i = 1; i <= numNums; i++) {
                System.out.print("Case #" + i + ": ");
                solve(reader.readLine());
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void solve(final String numStr) {
        final int len = numStr.length();
        final FourlessNumber check1 = new FourlessNumber(len);
        final FourlessNumber check2 = new FourlessNumber(len);

        boolean mustCarry = false;
        boolean prevMustCarry = false;
        for (int i = 0; i < len; i++) {
            final byte val = Byte.parseByte(numStr.substring(i, i + 1));

            if (mustCarry) {
                check1.initDigit(i, (byte)(val/2 + 5));
                check2.initDigit(i, (byte)(val/2 + 5));
            }
            else {
                check1.initDigit(i, (byte)(val/2));
                check2.initDigit(i, (byte)(val/2));
            }

            prevMustCarry = mustCarry;
            mustCarry = val % 2 == 1;
        }

        if (mustCarry) {
            check1.bump();
        }

        if (check1.fourless() && check2.fourless()) {
            print(check1, check2);
            return;
        }

        FourlessNumber difference = check1.nextOption(FourlessNumber.EMPTY);
        do {
            difference = check2.prevOption(difference);
            difference = check1.nextOption(difference);
        } while (!difference.isZero());

        print(check1, check2);
    }

    private static void print(FourlessNumber check1, FourlessNumber check2) {
        System.out.println(check1.toString() + ' ' + check2.toString());
    }
}
