import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class winFrame extends JFrame {
    slidingPuzzle sp = new slidingPuzzle();
    winFrame() throws IOException {
        setLayout(new FlowLayout());

        setSize(100,50);

    setSize(414,226);
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
        }
    });
}
}
