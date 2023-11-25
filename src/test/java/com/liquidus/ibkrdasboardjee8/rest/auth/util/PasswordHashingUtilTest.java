package com.liquidus.ibkrdasboardjee8.rest.auth.util;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static com.liquidus.ibkrdasboardjee8.rest.auth.util.PasswordHashingUtil.getUserPassword;
import static com.liquidus.ibkrdasboardjee8.rest.auth.util.PasswordHashingUtil.isMatched;
import static org.junit.jupiter.api.Assertions.*;

class PasswordHashingUtilTest {

    @Test
    public void testIsMatched() {
        String[] pwd = getUserPassword("josh");
        assertNotNull(pwd);

        // test valid password
        assertTrue(isMatched(pwd[0],
                "joshBest",
                Base64.getDecoder().decode(pwd[1]),
                Integer.parseInt(pwd[2])));

        // test invalid password
        assertFalse(isMatched(pwd[0],
                "joshBes",
                Base64.getDecoder().decode(pwd[1]),
                Integer.parseInt(pwd[2])));
    }
}