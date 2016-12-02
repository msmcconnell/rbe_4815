/*
 * This class was created by StackOverflow user Joffrey in 2013
 * The origional stack overflow question can be found at the following url
 * http://stackoverflow.com/questions/12945537/how-to-set-output-stream-to-textarea
 * The class serves to reroute an output stream to the provided JTextPane
 */
package rbe_4815_final_project;

import java.io.PrintStream;
import javax.swing.JTextPane;

public class PrintStreamCapturer extends PrintStream {

    private JTextPane text;
    private boolean atLineStart;
    private String indent;

    public PrintStreamCapturer(JTextPane textArea, PrintStream capturedStream, String indent) {
        super(capturedStream);
        this.text = textArea;
        this.indent = indent;
        this.atLineStart = true;
    }

    public PrintStreamCapturer(JTextPane textArea, PrintStream capturedStream) {
        this(textArea, capturedStream, "");
    }

    private void writeToTextPane(String str) {
        if (text != null) {
            synchronized (text) {
                text.setCaretPosition(text.getDocument().getLength());
                text.setText(text.getText().concat(str));
            }
        }
    }

    private void write(String str) {
        String[] s = str.split("\n", -1);
        if (s.length == 0)
            return;
        for (int i = 0; i < s.length - 1; i++) {
            writeWithPotentialIndent(s[i]);
            writeWithPotentialIndent("\n");
            atLineStart = true;
        }
        String last = s[s.length - 1];
        if (!last.equals("")) {
            writeWithPotentialIndent(last);
        }
    }

    private void writeWithPotentialIndent(String s) {
        if (atLineStart) {
            writeToTextPane(indent + s);
            atLineStart = false;
        } else {
            writeToTextPane(s);
        }
    }

    private void newLine() {
        write("\n");
    }

    @Override
    public void print(boolean b) {
        synchronized (this) {
            super.print(b);
            write(String.valueOf(b));
        }
    }

    @Override
    public void print(char c) {
        synchronized (this) {
            super.print(c);
            write(String.valueOf(c));
        }
    }

    @Override
    public void print(char[] s) {
        synchronized (this) {
            super.print(s);
            write(String.valueOf(s));
        }
    }

    @Override
    public void print(double d) {
        synchronized (this) {
            super.print(d);
            write(String.valueOf(d));
        }
    }

    @Override
    public void print(float f) {
        synchronized (this) {
            super.print(f);
            write(String.valueOf(f));
        }
    }

    @Override
    public void print(int i) {
        synchronized (this) {
            super.print(i);
            write(String.valueOf(i));
        }
    }

    @Override
    public void print(long l) {
        synchronized (this) {
            super.print(l);
            write(String.valueOf(l));
        }
    }

    @Override
    public void print(Object o) {
        synchronized (this) {
            super.print(o);
            write(String.valueOf(o));
        }
    }

    @Override
    public void print(String s) {
        synchronized (this) {
            super.print(s);
            if (s == null) {
                write("null");
            } else {
                write(s);
            }
        }
    }

    @Override
    public void println() {
        synchronized (this) {
            newLine();
            super.println();
        }
    }

    @Override
    public void println(boolean x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(char x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(int x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(long x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(float x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(double x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(char x[]) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(String x) {
        synchronized (this) {
            print(x);
            newLine();
            super.println();
        }
    }

    @Override
    public void println(Object x) {
        String s = String.valueOf(x);
        synchronized (this) {
            print(s);
            newLine();
            super.println();
        }
    }
}
