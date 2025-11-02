package com.cobolairlines.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// Experimental test: try several LCG parameters and seed interpretations
public class CryptoParityFinderTest {

    @Test
    public void findMatchingRngParams() throws Exception {
        File f = new File("..\\COBOL-AIRLINES\\COB-PROG\\EMPLO-INSERT\\EMPLO-OUTPUT-PASS-CRYPT");
        if (!f.exists()) {
            System.out.println("Legacy vector file not found, skipping parity search.");
            return;
        }
        List<String> lines = Files.readAllLines(f.toPath());
        List<String> empids = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            if (line.length() < 9) continue;
            empids.add(line.substring(0,8));
        }
        if (empids.isEmpty()) {
            System.out.println("No empids found in legacy file.");
            return;
        }

        String testEmp = empids.get(0);
        System.out.println("Testing empid=" + testEmp);

        // candidate LCG parameters: multiplier, increment, modulus
        long[][] candidates = new long[][]{
                {1103515245L, 12345L, (1L<<31)},
                {1664525L, 1013904223L, (1L<<32)},
                {25214903917L, 11L, (1L<<48)},
                {214013L, 2531011L, (1L<<31)}
        };

        // try seed interpretations
        String[] seeds = new String[]{"20221102", "20221102", "221102", "19700101"};

        for (long[] params : candidates) {
            for (String seedStr : seeds) {
                long seed;
                try { seed = Long.parseLong(seedStr);} catch (Exception e) { seed = seedStr.hashCode(); }
                long val = lcgNext(seed, params[0], params[1], params[2]);
                double d = (double)(val & 0x7fffffffffffffffL) / (double)params[2];
                int wsKey = (int)(d * 1000);
                System.out.printf("Params a=%d c=%d m=%d seed=%s -> wsKey=%d\n", params[0], params[1], params[2], seedStr, wsKey);
            }
        }

        System.out.println("Done. This is an experimental harness — refine and match the full algorithm next.");
    }

    private long lcgNext(long seed, long a, long c, long m) {
        return (a * seed + c) % m;
    }
}
