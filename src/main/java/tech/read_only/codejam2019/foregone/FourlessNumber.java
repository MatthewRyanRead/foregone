package tech.read_only.codejam2019.foregone;

@SuppressWarnings("WeakerAccess")
public class FourlessNumber {
    private final byte[] num;
    private int highest4Index = -1;
    private int mostSigIndex;
    private boolean isZero = true;

    public static final FourlessNumber EMPTY = new FourlessNumber(0);

    public FourlessNumber(int numDigits) {
        this.num = new byte[numDigits];
        this.mostSigIndex = numDigits - 1;
    }

    /**
     * Digits must be initialized from most significant (lowest index) to least (highest index)
     */
    public void initDigit(int index, byte value) {
        if (value == 0) return;

        this.isZero = false;

        if (this.mostSigIndex > index) {
            this.mostSigIndex = index;
        }

        if (value == 4 && highest4Index == -1) {
            this.highest4Index = index;
        }

        this.num[index] = value;
    }

    public void bump() {
        this.num[this.num.length - 1]++;
    }

    public boolean fourless() {
        return this.highest4Index < 0;
    }

    public boolean isZero() {
        return this.isZero;
    }

    /**
     * @param minDifference The minimum amount to increase this number by.
     *                      Must be zero, or have the same number of allocated digits.
     * @return The additional difference required to make this number fourless.
     */
    public FourlessNumber nextOption(final FourlessNumber minDifference) {
        if (this.highest4Index > minDifference.mostSigIndex) {
            final FourlessNumber difference = increaseFrom(this.highest4Index);
            this.highest4Index = -1;
            return difference;
        }

        if (minDifference.isZero) {
            return minDifference;
        }

        return increaseBy(minDifference);
    }

    /**
     * @param minDifference The minimum amount to decrease this number by.
     *                      Must be zero, or have the same number of allocated digits.
     * @return The additional difference required to make this number fourless.
     */
    public FourlessNumber prevOption(final FourlessNumber minDifference) {
        if (this.highest4Index > minDifference.mostSigIndex) {
            final FourlessNumber difference = decreaseFrom(this.highest4Index);
            this.highest4Index = -1;
            return difference;
        }

        if (minDifference.isZero) {
            return minDifference;
        }

        return decreaseBy(minDifference);
    }

    private FourlessNumber increaseFrom(final int index) {
        final FourlessNumber difference = new FourlessNumber(this.num.length);

        boolean carry = false;
        for (int i = this.num.length - 1; i > index; i--) {
            byte val = (byte)(10 - this.num[i] - (carry ? 1 : 0));
            if (val == 10) {
                carry = false;
            }
            else {
                carry = true;
                difference.initDigit(i, val);
            }

            this.num[i] = 0;
        }
        this.num[index]++;

        if (index == this.num.length - 1) {
            difference.initDigit(this.num.length - 1, (byte)1);
        }

        return difference;
    }

    private FourlessNumber decreaseFrom(final int index) {
        final FourlessNumber difference = new FourlessNumber(this.num.length);

        boolean carry = true;
        for (int i = this.num.length - 1; i > index; i--) {
            byte val = (byte)(this.num[i] + (carry ? 1 : 0));
            if (val == 10) {
                carry = true;
            }
            else {
                carry = false;
                difference.initDigit(i, val);
            }
            this.num[i] = 9;
        }
        this.num[index]--;

        if (index == this.num.length - 1) {
            difference.initDigit(this.num.length - 1, (byte)1);
        }

        return difference;
    }

    private FourlessNumber increaseBy(final FourlessNumber minDifference) {
        boolean carry = false;
        int high4Index = this.num.length;
        for (int i = minDifference.num.length - 1; i >= 0; i--) {
            int result = minDifference.num[i] + this.num[i] + (carry ? 1 : 0);

            if (result >= 10) {
                carry = true;
                result -= 10;
            }
            else {
                carry = false;
            }

            if (result == 4) {
                high4Index = i;
            }

            this.num[i] = (byte)result;
        }

        if (high4Index >= this.num.length) {
            return EMPTY;
        }

        return this.increaseFrom(high4Index);
    }

    private FourlessNumber decreaseBy(final FourlessNumber minDifference) {
        boolean carry = false;
        int high4Index = this.num.length;
        for (int i = minDifference.num.length - 1; i >= 0; i--) {
            int result = this.num[i] - minDifference.num[i] - (carry ? 1 : 0);

            if (result < 0) {
                carry = true;
                result += 10;
            }
            else {
                carry = false;
            }

            if (result == 4) {
                high4Index = i;
            }

            this.num[i] = (byte)result;
        }

        if (high4Index >= this.num.length) {
            return EMPTY;
        }

        return this.decreaseFrom(high4Index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean leadingZero = true;
        for (byte b : num) {
            if (b == 0 && leadingZero) continue;

            leadingZero = false;
            sb.append(b);
        }
        return sb.toString();
    }
}
