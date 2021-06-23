
// ***************************************************

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stopwatch {
    Label timeLabel = new Label();
    slidingPuzzle sp;
    int elapsedTime = 0;
    int seconds =0;
    int minutes =0;
    String recordTime = "";
    boolean started = false;
    String seconds_string = String.format("%02d", seconds);
    String minutes_string = String.format("%02d", minutes);
    int currentSeconds;
    Timer timer = new Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent e) {

            elapsedTime=elapsedTime+1000;
            minutes = (elapsedTime/60000) % 60;
            seconds = (elapsedTime/1000) % 60;
            seconds_string = String.format("%02d", seconds);
            minutes_string = String.format("%02d", minutes);
            timeLabel.setText(minutes_string+":"+seconds_string);

        }

    });



    Stopwatch(){
        timeLabel.setText(minutes_string+":"+seconds_string);
        timeLabel.setBounds(100,100,200,100);
        timeLabel.setFont(new Font("Verdana",Font.PLAIN,35));
    }


    void start() {
        timer.start();
    }
    void stop() {
        timer.stop();
       // System.out.println(timeLabel.getText());
            }
     String getRecordTime() {
       return timeLabel.getText();
     }



    public static void main(String[] args) {
        Stopwatch sw = new Stopwatch();
    }

}