import java.math.BigInteger;

public class Fibonacci {

    public static BigInteger[][] multiply2x2Matrices(BigInteger[][] a, BigInteger[][] b){
        BigInteger c00 = a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
        BigInteger c01 = a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
        BigInteger c10 = a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
        BigInteger c11 = a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));
        return new BigInteger[][] {{c00, c01},{c10, c11}};
    }

    public static BigInteger[][] matPower(BigInteger[][] mat, BigInteger exponent) {
        BigInteger[][] martrix = [[1,1],[1,0]];
    }

    public static BigInteger getNthFibonacciNumber(BigInteger n) {
        BigInteger[][] b = new BigInteger[][] {{BigInteger.ZERO, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ONE}};
        return matPower(b, n)[?][?]; // Replace the question marks by correct values.
    }

    public static void main(String[] args) {
        System.out.println(getNthFibonacciNumber(BigInteger.valueOf(10000)));
    }
}