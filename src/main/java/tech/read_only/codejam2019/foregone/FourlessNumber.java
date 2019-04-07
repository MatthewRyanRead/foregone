package tech.read_only.codejam2019.foregone;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings("WeakerAccess")
public class FourlessNumber {
    public static final FourlessNumber NAN = new FourlessNumber(0);

    private static final Pattern LEADING_ZEROES = Pattern.compile("^0+");

    private final byte[] num;
    private final int maxDigits;

    private int mostSig4Index = Integer.MIN_VALUE;
    private boolean isZero = true;

    private int mostSigIndex;

    // region init

    public FourlessNumber(int maxDigits) {
        this.num = new byte[maxDigits];
        this.maxDigits = maxDigits;
        this.mostSigIndex = maxDigits - 1;
    }

    public FourlessNumber(final FourlessNumber other) {
        this.num = Arrays.copyOf(other.num, other.num.length);
        this.maxDigits = other.maxDigits;
        this.mostSig4Index = other.mostSig4Index;
        this.isZero = other.isZero;
        this.mostSigIndex = other.mostSigIndex;
    }

    public void init(String str) {
        str = LEADING_ZEROES.matcher(str).replaceFirst("");
        if (str.isEmpty()) return;

        checkArgument(str.length() <= this.maxDigits, "Input has too many digits.");

        this.isZero = false;

        final int lenDifference = this.maxDigits - str.length();
        for (int i = 0; i < str.length(); i++) {
            this.setDigit(i + lenDifference, Byte.valueOf(str.substring(i, i + 1)));
        }
    }

    private void setDigit(final int index, final byte value) {
        if (value == 4) {
            if (this.mostSig4Index > index || this.mostSig4Index == Integer.MIN_VALUE) {
                this.mostSig4Index = index;
            }
        }
        else if (this.mostSig4Index == index) {
            for (int i = index + 1; i < this.maxDigits; i++) {
                if (this.num[i] == 4) {
                    this.mostSig4Index = i;
                    break;
                }
            }

            if (this.mostSig4Index == index) {
                this.mostSig4Index = Integer.MIN_VALUE;
            }
        }

        this.isZero = false;
        if (value == 0) {
            this.num[index] = 0;
            while (this.mostSigIndex < this.maxDigits && this.num[this.mostSigIndex] == 0) {
                this.mostSigIndex++;
            }

            if (this.mostSigIndex == this.maxDigits) {
                this.isZero = true;
            }
        }
        else if (this.mostSigIndex > index) {
            this.mostSigIndex = index;
        }
        this.num[index] = value;
    }

    public void increment() {
        this.isZero = false;
        for (int i = this.maxDigits - 1; i >= 0; i--) {
            if (this.num[i] == 9) {
                this.setDigit(i, (byte)0);
            }
            else {
                this.setDigit(i, (byte)(this.num[i] + 1));
                break;
            }
        }
    }

    // endregion init

    // region getters

    public boolean isFourless() {
        return this.mostSig4Index < 0;
    }

    public boolean isZero() {
        return this.isZero;
    }

    // endregion getters

    // region solvers

    /**
     * @param minDifference The minimum amount to increase this number by.
     *                      Must be zero, or have the same number of allocated digits.
     * @return The additional difference required to make this number fourless.
     */
    public FourlessNumber nextNumber(final FourlessNumber minDifference) {
        return this.findNumber(minDifference, this::increaseFromIndex, this::increaseByMinimum);
    }

    /**
     * @param minDifference The minimum amount to decrease this number by.
     *                      Must be zero, or have the same number of allocated digits.
     * @return The additional difference required to make this number fourless.
     */
    public FourlessNumber prevNumber(final FourlessNumber minDifference) {
        return this.findNumber(minDifference, this::decreaseFromIndex, this::decreaseByMinimum);
    }

    private FourlessNumber findNumber(final FourlessNumber minDifference,
                                      final Function<Integer, FourlessNumber> alterFromIndex,
                                      final Function<FourlessNumber, FourlessNumber> alterBy) {
        if (this.mostSig4Index > minDifference.mostSigIndex) {
            return alterFromIndex.apply(this.mostSig4Index);
        }

        if (this.isFourless() && minDifference.isZero) {
            return FourlessNumber.NAN;
        }

        return alterBy.apply(minDifference);
    }

    // endregion solvers

    // region solution helpers

    private FourlessNumber increaseFromIndex(final int index) {
        return this.changeFrom(index,
                               false,
                               digit -> (byte)(10 - digit),
                               carry -> carry ? -1 : 0,
                               0,
                               sigDigit -> sigDigit + 1);
    }

    private FourlessNumber decreaseFromIndex(final int index) {
        return this.changeFrom(index,
                               true,
                               digit -> digit,
                               carry -> carry ? 1 : 0,
                               9,
                               sigDigit -> sigDigit - 1);
    }

    private FourlessNumber changeFrom(final int index,
                                      final boolean initialCarry,
                                      final Function<Byte, Byte> differenceAddition,
                                      final Function<Boolean, Integer> carryAddition,
                                      final int defaultDigit,
                                      final Function<Byte, Integer> sigDigitEffect) {
        final FourlessNumber difference = new FourlessNumber(this.maxDigits);

        boolean carry = initialCarry;
        for (int i = this.maxDigits - 1; i > index; i--) {
            final byte val = (byte)(differenceAddition.apply(this.num[i]) + carryAddition.apply(carry));
            if (val == 10) {
                carry = initialCarry;
            }
            else {
                carry = !initialCarry;
                difference.setDigit(i, val);
            }

            this.setDigit(i, (byte)defaultDigit);
        }
        this.setDigit(index, sigDigitEffect.apply(this.num[index]).byteValue());

        if (index == this.maxDigits - 1) {
            difference.setDigit(this.maxDigits - 1, (byte)1);
        }

        if (difference.isZero) {
            difference.setDigit(index, (byte)1);
        }

        return difference;
    }

    public FourlessNumber increaseByMinimum(final FourlessNumber minDifference) {
        return this.changeBy(minDifference,
                             difference -> difference,
                             carry -> carry ? 1 : 0,
                             result -> result >= 10,
                             result -> result - 10,
                             this::increaseFromIndex);
    }

    public FourlessNumber decreaseByMinimum(final FourlessNumber minDifference) {
        return this.changeBy(minDifference,
                             difference -> (byte) -difference,
                             carry -> carry ? -1 : 0,
                             result -> result < 0,
                             result -> result + 10,
                             this::decreaseFromIndex);
    }

    private FourlessNumber changeBy(final FourlessNumber minDifference,
                                    final Function<Byte, Byte> differenceAddition,
                                    final Function<Boolean, Integer> carryAddition,
                                    final Function<Integer, Boolean> carryCheck,
                                    final Function<Integer, Integer> carryEffect,
                                    final Function<Integer, FourlessNumber> finalStep) {
        boolean carry = false;
        int high4Index = this.maxDigits;
        for (int i = minDifference.num.length - 1; i >= 0; i--) {
            int result = this.num[i] + differenceAddition.apply(minDifference.num[i])
                                     + carryAddition.apply(carry);

            carry = carryCheck.apply(result);
            if (carry) {
                result = carryEffect.apply(result);
            }

            if (result == 4) {
                high4Index = i;
            }

            this.setDigit(i,  (byte)result);
        }

        if (high4Index >= this.maxDigits) {
            return FourlessNumber.NAN;
        }

        return finalStep.apply(high4Index);
    }

    // endregion solution helpers

    // region standard overrides

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (byte b : this.num) {
            sb.append(b);
        }
        return LEADING_ZEROES.matcher(sb.toString()).replaceFirst("");
    }

    // endregion standard overrides
}
