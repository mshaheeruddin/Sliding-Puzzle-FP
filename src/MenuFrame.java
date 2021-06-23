import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

public class MenuFrame extends Frame {
    BufferedImage bi;
    slidingPuzzle sp;

    Button startButton = new Button("Start Game");
    Button recordButton = new Button("See Records");
    TextField tf = new TextField();
    PriorityQueue<Player> priorityQueue = new PriorityQueue<>();


    MenuFrame() throws IOException {
        Label enterName = new Label();
        enterName.setText("Enter Your Name: ");
        enterName.setBounds(50,230,100,25);
        enterName.setBackground(new Color(0xE7E75B));
        add(enterName);
        //image for background
        setBackground(Color.PINK);
       setBounds(350,30,389,360);
       bi = ImageIO.read(new File("imgOfMenu.png"));
       //button
        startButton.setFocusable(false);
        startButton.setBounds((getWidth()/2)+30,(getHeight()/2)+100,100,30);
        add(startButton);
        setLayout(null);
        recordButton.setFocusable(false);
        recordButton.setBounds((getWidth()/2)-150,(getHeight()/2)+100,100,30);
        add(recordButton);

        //TextField
        tf.setBounds((getWidth()/2)-20,(getHeight()/2)+50,150,25);
        tf.setEditable(true);
        add(tf);


        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("PlayerRecord.txt");
                FileReader myReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(myReader);
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        String[] result = line.split("-");
                           Player p = new Player(result[0],Integer.parseInt(result[1]),Integer.parseInt(result[2]));
                           priorityQueue.add(p);
                    }
                    int size = priorityQueue.size();
                    for (int i = 0;i<size;i++) {
                        System.out.println(priorityQueue.poll());
                    }
                    reader.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }}
        });






        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false); dispose();
                System.exit(0);
            }
        });

       setVisible(true);

   }


   public void paint(Graphics g) {
       g.drawImage(bi,0,0,getWidth(),getHeight(),null);
   }

    public static void main(String[] args) throws IOException {
    }

}
