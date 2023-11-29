package com.example.stylespo;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.stylespo.model.User;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void emailValidatorCorrect() {assertTrue(User.isValidEmail(("g.lemming13@gmail.com")));
    }

    @Test
    public void emailValidatorInCorrect() {assertFalse(User.isValidEmail(("g.lemming13gmail.com")));
    }

    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {assertFalse(User.isValidEmail(null));
    }

    @Test
    public void isDOBValidCorrect() {assertTrue(User.isDOBValid("01/15/1990"));
    }
    @Test
    public void isDOBValidNull() {assertFalse(User.isDOBValid(null));
    }
    @Test
    public void testValidFirstName() {
        // Arrange
        String FirstName = "John";
        User user = new User("john@example.com", FirstName, "Doe", "01/01/1990");

        // Act
        boolean isValid = user.isValid();

        // Assert
        assertFalse(isValid);
    }
}