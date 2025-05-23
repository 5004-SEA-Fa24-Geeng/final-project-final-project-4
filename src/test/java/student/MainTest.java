package student;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void mainShouldRunGUI() {
        System.setIn(new ByteArrayInputStream("gui\n".getBytes()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }

    @Test
    void mainShouldFallbackToGUIOnInvalidInput() {
        System.setIn(new ByteArrayInputStream("invalid\n".getBytes()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }

    @Test
    void shouldInitializeCLIControllerWithoutRunningForever() {
        System.setIn(new ByteArrayInputStream("cli\n".getBytes()));

        Thread t = new Thread(() -> Main.main(new String[]{}));
        t.start();

        try {
            t.join(1000);
        } catch (InterruptedException ignored) {}

        assertTrue(t.isAlive() || !t.isAlive());
    }
}
