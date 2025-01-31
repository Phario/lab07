package pl.pwr.ite.dynak.gui;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.OutputStream;
import java.io.PrintStream;

public interface IRedirector {
    default void redirectConsoleOutput(TextArea textArea) {
        OutputStream out = new OutputStream() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                if (b == '\n') {
                    String text = buffer.toString();
                    buffer.setLength(0);
                    Platform.runLater(() -> textArea.appendText(text + "\n"));
                } else {
                    buffer.append((char) b);
                }
            }
        };
        PrintStream consoleStream = new PrintStream(out, true);
        System.setOut(consoleStream);
        System.setErr(consoleStream);
    }
}
