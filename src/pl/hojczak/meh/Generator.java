/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hojczak
 */
public class Generator {

 
    public static void main(String[] args) {
        Generator gen = new Generator(Integer.parseInt(args[0]),
                Long.parseLong(args[1]));
        int maxIteration = Integer.parseInt(args[2]);
        List<Long> iteration = new ArrayList<>(maxIteration);

        for (int i = 0; i < maxIteration; i++) {
            iteration.add(gen.next());
        }
        long srednia = 0;
        long odchylenie=0;
        for (Long i : iteration) {
            srednia += i; 
        }
        for (Long i : iteration) {
            odchylenie+=srednia-i; 
        }
        odchylenie/=maxIteration;
        
        odchylenie=Math.abs(odchylenie);
        
        srednia /= maxIteration;

        System.out.println("Wartość oczekiwana: " + srednia);
        System.out.println("Odchylenie standardowe: " + odchylenie);

    }

    private int memberMax;
    private long[] member;
    private long[] a;
    private long c;
    private long m;
    private int index;

    public Generator(int paramK, long paramM) {
        long seed = new Date().getTime();
        System.out.println("seed: "+seed);
        System.out.println("k: "+paramK);
        System.out.println("m: "+paramM);
        memberMax = paramK;
        member = new long[memberMax];
        a = new long[memberMax];
        for (int i = 0; i < memberMax; i++) {
            member[i] = (seed * i) % 23;
            a[i] = (seed * i) % 113;
        }
        c = 5857 * 19 & 7621;
        m = paramM;
    }

    public long next() {
        long number = 0;
        for (int i = 0; i < memberMax; i++) {
            number += a[i] * member[i];
        }
        
        number += c;
        number %= m;
        member[(index%memberMax)]=number;
        index++;
        return number;
    }

}
