package com.cobolairlines.service.impl;

import com.cobolairlines.service.CryptoService;
import org.springframework.stereotype.Service;

/**
 * Initial CryptoService implementation placeholder.
 *
 * IMPORTANT: This implementation is a placeholder and must be replaced with a
 * faithful port of the COBOL `CRYPTO-VERIFICATION` routine. Unit tests with
 * legacy test vectors must be added to verify parity.
 */
@Service
public class CryptoServiceImpl implements CryptoService {

    @Override
    public String produceCryptPass(String empid, String password, String admidate) {
        // Port of COBOL CRYPTVE routine (CRYPTO-VERIFICATION)
        // Best-effort direct translation of the COBOL logic found in
        // CICS/LOGIN/CRYPTO-VERIFICATION. This follows the original steps:
        // - build a numeric seed from admindate (YYYY-MM-DD or YYYYMMDD)
        // - generate a deterministic pseudo-random WS-KEY from that seed
        // - iterate up to 8 positions and produce 8 crypt characters
        // The COBOL source uses low-level numeric COMP operations and
        // FUNCTION RANDOM. We emulate the control flow and character
        // selections (the crypt chars are taken from password/userid bytes
        // depending on a position-based modulus) to reproduce behavior.

        if (empid == null) empid = "";
        if (password == null) password = "";
        if (admidate == null) admidate = "";

        // Normalize inputs to lengths used by COBOL (1-based indexing in substrings)
        String lsUserId = (empid.length() >= 8) ? empid.substring(0, 8) : String.format("%-8s", empid);
        String lsPassword = (password.length() >= 8) ? password.substring(0, 8) : String.format("%-8s", password);

        // Build WS-DATE as YYYYMMDD by extracting positions used in COBOL
        // COBOL used (1:4),(6:2),(9:2) which matches YYYY-MM-DD or YYYYMMDD
        String digits = admidate.replaceAll("[^0-9]", "");
        String wsDate = "00000000";
        if (digits.length() >= 8) {
            wsDate = digits.substring(0, 8);
        } else if (digits.length() == 6) {
            // maybe YYMMDD, prepend 19 or 20? fallback: pad
            wsDate = String.format("%08d", Integer.parseInt(digits));
        }

        // Emulate FUNCTION RANDOM(seed) * 1000 with Java Random seeded by wsDate
        long seed;
        try {
            seed = Long.parseLong(wsDate);
        } catch (Exception e) {
            seed = wsDate.hashCode();
        }
        java.util.Random rand = new java.util.Random(seed);
        int wsKey = (int) (rand.nextDouble() * 1000);

        // working variables
        int wsPass = 0;
        int wsKey1 = wsKey;
        int wsUserId1 = 0;

        char[] crypt = new char[8];
        for (int i = 0; i < 8; i++) crypt[i] = '\u0000';

        // COBOL loops are 1-based
        for (int counter1 = 1; counter1 <= 8; counter1++) {
            char pwdChar = lsPassword.length() >= counter1 ? lsPassword.charAt(counter1 - 1) : ' ';
            if (pwdChar == ' ') break; // STOP when password char is space

            // In COBOL an inner loop increments counter2 and checks cryptbyte values.
            // For the port we perform a single pass matching the effective behavior.
            int counter2 = counter1;
            int wsMod1 = counter2 % 3;

            // extract 2-char windows (COBOL used (pos:2) and then (2:1) to get the second char)
            String pwdWindow = (counter1 + 1 <= lsPassword.length()) ? lsPassword.substring(counter1 - 1, Math.min(counter1 + 1, lsPassword.length())) : (lsPassword.charAt(counter1 - 1) + " ");
            String uidWindow = (counter1 + 1 <= lsUserId.length()) ? lsUserId.substring(counter1 - 1, Math.min(counter1 + 1, lsUserId.length())) : (lsUserId.charAt(counter1 - 1) + " ");

            char takeChar;
            switch (wsMod1) {
                case 0:
                    // COMPUTE WS-KEY1 = WS-PASS * WS-KEY1; MOVE WS-KEY1 TO WS-PASS
                    wsKey1 = safeMul(wsPass, wsKey1);
                    wsPass = wsKey1;
                    // MOVE WS-PASSWORD(2:1) TO WS-CRYPTCHAR
                    takeChar = pwdWindow.length() >= 2 ? pwdWindow.charAt(1) : pwdWindow.charAt(0);
                    crypt[counter1 - 1] = takeChar;
                    break;
                case 1:
                    // COMPUTE WS-USERID1 = WS-PASS * WS-USERID1
                    wsUserId1 = safeMul(wsPass, wsUserId1);
                    // MOVE WS-USERID(2:1) TO WS-CRYPTCHAR
                    takeChar = uidWindow.length() >= 2 ? uidWindow.charAt(1) : uidWindow.charAt(0);
                    crypt[counter1 - 1] = takeChar;
                    break;
                case 2:
                    // COMPUTE  WS-KEY1 = WS-PASS * WS-USERID1 * WS-KEY1
                    wsKey1 = safeMul(wsPass, safeMul(wsUserId1, wsKey1));
                    wsPass = wsKey1;
                    // MOVE WS-PASSWORD(2:1) TO WS-CRYPTCHAR
                    takeChar = pwdWindow.length() >= 2 ? pwdWindow.charAt(1) : pwdWindow.charAt(0);
                    crypt[counter1 - 1] = takeChar;
                    break;
                default:
                    crypt[counter1 - 1] = '?';
            }
        }

        // Build string result of length 8 (COBOL printed/displayed the 8 bytes)
        return new String(crypt);
    }

    private int safeMul(int a, int b) {
        // keep values in a reasonable 32-bit signed range similar to COBOL COMP behaviors
        long r = (long) a * (long) b;
        return (int) (r & 0x7fffffff);
    }

}
