/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.io.Console;

/**
 *
 * @author hojczak
 */
public class Generator {

    private static long m;
    private static long a;
    private static long c;
    private static long x;

    //Calculate a*x mod m
    private static long mult(long a, long x, long m) {
        long b, n, r;

        r = 0;
        n = 1;
        b = 1;
        while (n <= 64) {
            if ((a & b) != 0) {
                r = (r + x) % m;
            }
            x = (x + x) % m;
            b *= 2;
            n++;
        }

        return r;
    }

    public static long genRand() {
        x = (mult(a, x, m) + c) % m;
        return x;
    }

    public static void main(String[] args) {
        int i, n;
        long low, high;

        System.out.println("Podaj wspolczynnik m");
      
        m = readLong();

        System.out.println("Podaj wspolczynnik a");
        a = readLong();

        System.out.println("Podaj wspolczynnik c");
        c = readLong();

        System.out.println("Podaj wartosc ziarna x[1]");
        x = readLong();

        System.out.println("Podaj ile liczb pseudolosowych wylosowac");
        n = readInt();

        System.out.println("Podaj dolna wartosc zakresu generowanych liczb");
        low = readLong();

        System.out.println("Podaj gorna wartosc zakresu generowanych liczb");
        high = readLong();

        for (i = 0; i < n; i++) {
            System.out.println(low + (genRand() % (high - low + 1)));
        }

    }
    
    private static long readLong(){
      return Long.parseLong(System.console().readLine());
    }
    private static int readInt(){
      return Integer.parseInt(System.console().readLine());
    }
}
