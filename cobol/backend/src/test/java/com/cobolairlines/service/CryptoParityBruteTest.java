package com.cobolairlines.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CryptoParityBruteTest {

    @Test
    public void bruteForceLcgs() throws Exception {
        File f = new File("..\\COBOL-AIRLINES\\COB-PROG\\EMPLO-INSERT\\EMPLO-OUTPUT-PASS-CRYPT");
        if (!f.exists()) {
            System.out.println("Legacy vector file not found, skipping.");
            return;
        }
        byte[] all = Files.readAllBytes(f.toPath());
        String[] rawLines = new String(all, java.nio.charset.StandardCharsets.ISO_8859_1).split("\r?\n");
        List<byte[]> empidsBytes = new ArrayList<>();
        List<byte[]> cryptsBytes = new ArrayList<>();
        for (String line : rawLines) {
            if (line.trim().isEmpty()) continue;
            byte[] lb = line.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1);
            if (lb.length < 9) continue;
            byte[] id = new byte[8];
            System.arraycopy(lb, 0, id, 0, 8);
            byte[] crypt = new byte[lb.length - 8];
            System.arraycopy(lb, 8, crypt, 0, lb.length - 8);
            empidsBytes.add(id);
            cryptsBytes.add(crypt);
        }
        if (empidsBytes.isEmpty()) return;

        // candidate LCG parameters
        long[][] candidates = new long[][]{
                {1103515245L, 12345L, (1L<<31)},
                {1664525L, 1013904223L, (1L<<32)},
                {25214903917L, 11L, (1L<<48)},
                {214013L, 2531011L, (1L<<31)}
        };

        String[] seedFormats = new String[]{"yyyyMMdd","yyMMdd","yyyyMMdd_lenient"};

        // limit to first 5 empids to keep test time reasonable
        int limit = Math.min(empidsBytes.size(), 5);
        for (int idx = 0; idx < limit; idx++) {
            String emp = new String(empidsBytes.get(idx), java.nio.charset.StandardCharsets.ISO_8859_1);
            byte[] legacyBytes = cryptsBytes.get(idx);
            System.out.println("Checking emp=" + emp + " legacy-bytes-length=" + legacyBytes.length);
            // Try brute-forcing wsKey (0..999) and several integer masks (simulate different COMP widths)
            int[] masks = new int[]{0xffff, 0xffffffff, 0x7fffffff, 0xffffffff};
            String[] admindates = new String[]{"20221102","19700101","20220101"};
            boolean found = false;
            for (String adm : admindates) {
                for (int mask : masks) {
                    for (int k = 0; k < 1000; k++) {
                        byte[] producedBytes = produceCryptWithWsKeyAndMask(emp, "", adm, k, mask);
                        if (java.util.Arrays.equals(producedBytes, legacyBytes)) {
                            System.out.printf("MATCH! emp=%s mask=0x%X wsKey=%d adm=%s\n", emp, mask, k, adm);
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
                if (found) break;
            }
        }
        System.out.println("No exact match found with tested parameters.");
    }

    private long parseSeed(String adm, String fmt) {
        String digits = adm.replaceAll("[^0-9]", "");
        if (fmt.equals("yyMMdd") && digits.length() >= 6) return Long.parseLong(digits.substring(digits.length()-6));
        if (fmt.equals("yyyyMMdd_lenient") && digits.length() >= 8) return Long.parseLong(digits.substring(0,8));
        try { return Long.parseLong(digits); } catch (Exception e) { return digits.hashCode(); }
    }

    private long lcgNext(long seed, long a, long c, long m) {
        return (a * seed + c) % m;
    }

    private byte[] produceCryptWithWsKey(String empid, String password, String admidate, int wsKey) {
        String emp = empid == null ? "" : empid;
        String pwd = password == null ? "" : password;
        String adm = admidate == null ? "" : admidate;
        String lsUserId = (emp.length() >= 8) ? emp.substring(0,8) : String.format("%-8s", emp);
        String lsPassword = (pwd.length() >= 8) ? pwd.substring(0,8) : String.format("%-8s", pwd);

        int wsPass = 0;
        int wsKey1 = wsKey;
        int wsUserId1 = 0;
    byte[] crypt = new byte[8];
    for (int i=0;i<8;i++) crypt[i]=0x20; // space

        for (int counter1=1; counter1<=8; counter1++){
            char pwdChar = lsPassword.length() >= counter1 ? lsPassword.charAt(counter1-1) : ' ';
            if (pwdChar==' ') break;
            int counter2 = counter1;
            int iterations = 0;
            while (true) {
                iterations++; if (iterations>256) break;
                int wsMod1 = counter2 % 3;
                String pwdWindow = (counter1 + 1 <= lsPassword.length()) ? lsPassword.substring(counter1 - 1, Math.min(counter1 + 1, lsPassword.length())) : (lsPassword.charAt(counter1 - 1) + " ");
                String uidWindow = (counter1 + 1 <= lsUserId.length()) ? lsUserId.substring(counter1 - 1, Math.min(counter1 + 1, lsUserId.length())) : (lsUserId.charAt(counter1 - 1) + " ");
                    int candidate;
                switch (wsMod1) {
                    case 0:
                        wsKey1 = safeMul(wsPass, wsKey1);
                        wsPass = wsKey1 & 0xffff;
                        // WS-PASSWORD(2:1) -> second byte (low-order)
                        candidate = wsPass & 0xff;
                        break;
                    case 1:
                        wsUserId1 = safeMul(wsPass, wsUserId1);
                        wsUserId1 = wsUserId1 & 0xffff;
                        candidate = wsUserId1 & 0xff;
                        break;
                    default:
                        wsKey1 = safeMul(wsPass, safeMul(wsUserId1, wsKey1));
                        wsPass = wsKey1 & 0xffff;
                        candidate = wsPass & 0xff;
                }
                int b = candidate & 0xff;
                if (b != 0x40 && b != 0x10 && b != 0x00 && b != 0x30 && b != 0x20) {
                    crypt[counter1-1] = (byte)b; break;
                }
                counter2++;
            }
        }
        return crypt;
    }

    private byte[] produceCryptWithWsKeyAndMask(String empid, String password, String admidate, int wsKey, int mask) {
        String emp = empid == null ? "" : empid;
        String pwd = password == null ? "" : password;
        String lsUserId = (emp.length() >= 8) ? emp.substring(0, 8) : String.format("%-8s", emp);
        String lsPassword = (pwd.length() >= 8) ? pwd.substring(0, 8) : String.format("%-8s", pwd);

        int wsPass = 0;
        int wsKey1 = wsKey;
        int wsUserId1 = 0;
    byte[] crypt = new byte[8];
    for (int i = 0; i < 8; i++) crypt[i] = 0x20;

        for (int counter1 = 1; counter1 <= 8; counter1++) {
            char pwdChar = lsPassword.length() >= counter1 ? lsPassword.charAt(counter1 - 1) : ' ';
            if (pwdChar == ' ') break;

            int counter2 = counter1;
            int iterations = 0;
            while (true) {
                iterations++;
                if (iterations > 256) break;
                int wsMod1 = counter2 % 3;
                String pwdWindow = (counter1 + 1 <= lsPassword.length()) ? lsPassword.substring(counter1 - 1, Math.min(counter1 + 1, lsPassword.length())) : (lsPassword.charAt(counter1 - 1) + " ");
                String uidWindow = (counter1 + 1 <= lsUserId.length()) ? lsUserId.substring(counter1 - 1, Math.min(counter1 + 1, lsUserId.length())) : (lsUserId.charAt(counter1 - 1) + " ");
                char candidate;
                switch (wsMod1) {
                    case 0:
                        wsKey1 = safeMulMask(wsPass, wsKey1, mask);
                        wsPass = wsKey1;
                        candidate = pwdWindow.length() >= 2 ? pwdWindow.charAt(1) : pwdWindow.charAt(0);
                        break;
                    case 1:
                        wsUserId1 = safeMulMask(wsPass, wsUserId1, mask);
                        candidate = uidWindow.length() >= 2 ? uidWindow.charAt(1) : uidWindow.charAt(0);
                        break;
                    default:
                        wsKey1 = safeMulMask(wsPass, safeMulMask(wsUserId1, wsKey1, mask), mask);
                        wsPass = wsKey1;
                        candidate = pwdWindow.length() >= 2 ? pwdWindow.charAt(1) : pwdWindow.charAt(0);
                }
                int b = candidate & 0xff;
                if (b != 0x40 && b != 0x10 && b != 0x00 && b != 0x30 && b != 0x20) {
                    crypt[counter1 - 1] = (byte)b;
                    break;
                }
                counter2++;
            }
        }
        return crypt;
    }

    private int safeMulMask(int a, int b, int mask) { long r = (long)a * (long)b; return (int)(r & (long)mask); }

    private int safeMul(int a, int b){ long r=(long)a*(long)b; return (int)(r & 0x7fffffff); }
}
