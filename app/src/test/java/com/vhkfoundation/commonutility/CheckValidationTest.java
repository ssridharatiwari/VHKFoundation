package com.vhkfoundation.commonutility;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckValidationTest {

    @Test
    public void testValidEmail() {
        assertTrue(CheckValidation.isEmailValid("user@example.com"));
    }

    @Test
    public void testEmailMissingAt() {
        assertFalse(CheckValidation.isEmailValid("userexample.com"));
    }

    @Test
    public void testEmailInvalidDomain() {
        assertFalse(CheckValidation.isEmailValid("user@example"));
    }
}
