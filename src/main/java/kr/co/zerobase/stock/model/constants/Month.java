package kr.co.zerobase.stock.model.constants;

public enum Month {
    JAN(1),
    FEB(2),
    MAR(3),
    APR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AUG(8),
    SEP(9),
    OCT(10),
    NOV(11),
    DEC(12);

    private final int number;

    Month(int number) {
        this.number = number;
    }

    public static int strToNumber(String s) throws IllegalArgumentException {
        return Month.valueOf(s.toUpperCase()).number;
    }
}
