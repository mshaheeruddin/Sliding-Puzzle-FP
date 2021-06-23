import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;


//import javax.swing.WindowConstants;



public class slidingPuzzle extends Frame  implements ActionListener, MouseListener, MouseMotionListener, ItemListener{ 
	int[][] grid;
	int height, width;
	int padding, noOfSquares;
	int inversions;
	Button shuffleButton, toggleLinesButton, toggleNumbersButton, selectImageButton;
	boolean isGameOver;
	int[][]solvedState;
	Panel gameOverPanel;
	BufferedImage[][] imageArr;
	BufferedImage[][] solvedImageArr;
	BufferedImage bufferedImage;
	File imageFile;
	String imagePath;
	Font font;
	boolean showLines;
	boolean showNumbers;
	JFileChooser fileChooser;
	BufferedImage completeBufferedImage;
	Graphics2D buildGraphics2d;
	BufferedImage linesBufferedImage;
	Graphics2D drawLinesGraphics2d;
	MediaTracker tracker;
	Stopwatch timer = new Stopwatch();
	MenuFrame mf = new MenuFrame();
	Label moveLabel = new Label();
	int mouseClicks = 0;
	String mouseClicks_string = String.format("%03d",mouseClicks);
	private String infoOfPlayer = "";

	public slidingPuzzle() throws IOException {
		// TODO Auto-generated constructor stub
		super("Sliding Puzzle");


		init();

		imagePath="image.png";
		initImageFile(imagePath);

		this.setLayout(null);
		this.setSize(new Dimension(2*padding+noOfSquares*width, 2*padding+noOfSquares*height));

		//		gameOverPanel = new Panel();
		//		this.add(gameOverPanel);
		//		gameOverPanel.setBounds(120, 100, 300, 300);
		//		Label gameOverLabel=new Label("You Win!",Label.CENTER);
		//		gameOverPanel.add(gameOverLabel);
		//		gameOverPanel.setVisible(false);
		//		gameOverPanel.setBackground(Color.GRAY);

		addMouseListener(this);
		addMouseMotionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false); dispose();
				System.exit(0);
			}
		});

		//Timer
		add(timer.timeLabel);
		timer.timeLabel.setBounds(10,0,150,100);
		//Moves
		//Move Label On right

		moveLabel.setText("Moves: "+mouseClicks_string);
		moveLabel.setAlignment(Label.CENTER);
		moveLabel.setBounds(getWidth()-padding-50,30,150,50);
		moveLabel.setBackground(Color.LIGHT_GRAY);
		add(moveLabel);

		//Text Field For Name
	}


	private void init() {
		// TODO Auto-generated method stub
		isGameOver=false;
		noOfSquares=3;
		inversions=0;
		padding=100;
		showLines=showNumbers=true;
		grid=new int[noOfSquares][noOfSquares];
		solvedState=new int[noOfSquares][noOfSquares];
		imageArr= new BufferedImage[noOfSquares][noOfSquares];
		solvedImageArr= new BufferedImage[noOfSquares][noOfSquares];
		tracker=new MediaTracker(this);
		font = new Font(null, Font.BOLD, 12);

		initGrid();
		for(int i=0;i<grid.length;i++) {
			solvedState[i]=grid[i].clone();
		}

		shuffleButton=new Button("Shuffle");
		shuffleButton.addActionListener(this);
		shuffleButton.setBounds(padding, this.getHeight()-padding+25, 100, 30);
		this.add(shuffleButton);

		toggleLinesButton=new Button("Toggle Lines");
		toggleLinesButton.addActionListener(this);
		toggleLinesButton.setBounds(shuffleButton.getX()+120, this.getHeight()-padding+25, 100, 30);
		this.add(toggleLinesButton);

		toggleNumbersButton=new Button("Toggle Numbers");
		toggleNumbersButton.addActionListener(this);
		toggleNumbersButton.setBounds(shuffleButton.getX()+240, this.getHeight()-padding+25, 100, 30);
		//System.out.println(getWidth()+" h:"+getHeight());
		this.add(toggleNumbersButton);

		selectImageButton=new Button("Select Image");
		selectImageButton.addActionListener(this);
		selectImageButton.setBounds(shuffleButton.getX()+360, this.getHeight()-padding+25, 100, 30);
		this.add(selectImageButton);

		//shuffleButton.setVisible(false);

		fileChooser = new JFileChooser("C:/Users/Dell/Pictures/");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setPreferredSize(new Dimension(700, 450));

	}
	private void initGrid() {
		// TODO Auto-generated method stub
		for (int i = 0; i < grid.length; i++) {
			for (int j=0;j<grid[i].length; j++) {
				grid[i][j]=noOfSquares*i+j+1;
			}
		}
		grid[noOfSquares-1][noOfSquares-1]=0;
	}

	public void initImageFile(String path) {
		imageFile = new File(path);
		initBufferedImage();

	}
	private void initBufferedImage() {
		// TODO Auto-generated method stub
		try {
			bufferedImage=ImageIO.read(imageFile);
			width=height=(Math.min(bufferedImage.getHeight(),bufferedImage.getWidth()))/noOfSquares;

			completeBufferedImage= new BufferedImage(width*noOfSquares, height*noOfSquares, BufferedImage.TYPE_INT_ARGB);
			buildGraphics2d= completeBufferedImage.createGraphics();
			buildGraphics2d.setPaint(Color.black);
			buildGraphics2d.setStroke(new BasicStroke(3.0f));
			tracker.addImage(completeBufferedImage, 0);

			drawLinesBufferedImage();
			initImageArr();
			//Shuffle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No file selected");
		}

	}
	private void drawLinesBufferedImage() {
		// TODO Auto-generated method stub
		linesBufferedImage= new BufferedImage(width*noOfSquares, height*noOfSquares, BufferedImage.TYPE_INT_ARGB);
		drawLinesGraphics2d=linesBufferedImage.createGraphics();
		drawLinesGraphics2d.setPaint(Color.black);
		drawLinesGraphics2d.setStroke(new BasicStroke(3.0f));
		for (int i=0; i<noOfSquares+1; i++) {
			drawLinesGraphics2d.drawLine(0, i*height, noOfSquares*width, i*height);
			drawLinesGraphics2d.drawLine(i*width,0, i*width, noOfSquares*height);
		}
		tracker.addImage(linesBufferedImage, 1);
	}
	public void initImageArr() {
		initGrid();
		for (int i = 0; i < imageArr.length; i++) {
			for (int j = 0; j < imageArr.length; j++) {
				imageArr[i][j]=bufferedImage.getSubimage(j*width, i*height, width, height);	
			}
			solvedImageArr[i]=imageArr[i].clone();
		}
		resizeFrame();
		//Shuffle();
		//updateImage();
	}

	private void resizeFrame() {
		// TODO Auto-generated method stub
		this.setSize(new Dimension(2*padding+noOfSquares*width, 2*padding+noOfSquares*height));
		shuffleButton.setBounds(padding, this.getHeight()-padding+25, 100, 30);
		//System.out.println(getHeight());
		toggleLinesButton.setBounds(shuffleButton.getX()+120, this.getHeight()-padding+25, 100, 30);
		toggleNumbersButton.setBounds(shuffleButton.getX()+240, this.getHeight()-padding+25, 100, 30);
		selectImageButton.setBounds(shuffleButton.getX()+360, this.getHeight()-padding+25, 100, 30);
	}
	public void updateImage() {
		if(buildGraphics2d==null)
			System.out.println("TRUE");
		for (int i = 0; i < noOfSquares; i++) {
			for (int j=0;j<noOfSquares; j++) {
				if(grid[i][j]!=0) {
					buildGraphics2d.drawImage(imageArr[i][j], j*width, i*height, null);
					if(imageArr[i][j]==null)
						System.out.println("i:"+i+"j:"+j);
					if(showNumbers) {
						buildGraphics2d.setFont(font);
						if(getLuminance(new Color(imageArr[i][j].getRGB(6, 10)))<125)
							buildGraphics2d.setPaint(Color.white);
						else {
							buildGraphics2d.setPaint(Color.black);
						}
						buildGraphics2d.drawString(grid[i][j]+"", 6+width*j, 19+height*i);
					}
				}
				else {
					buildGraphics2d.drawImage(solvedImageArr[i][j], j*width, i*height, null);
					if(!isGameOver) { 
						buildGraphics2d.setColor(new Color(0,0,0,200));
						buildGraphics2d.fillRect((width*j), (height*i), width, height);
						buildGraphics2d.setColor(Color.black);
					}
				}
			}
		}
		repaint();
	}

	File file = new File("PlayerRecord.txt");
	FileWriter myWriter = new FileWriter(file.getAbsoluteFile(), true);

	public int getLuminance(Color c) {
		return (int)(0.2126*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue());
	}
	public void paint(Graphics g) {
		update(g);

	}
	public void update(Graphics g) {
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tracker.checkAll()) {
			g.drawImage(completeBufferedImage, padding, padding, null);
			if(showLines)
				g.drawImage(linesBufferedImage, padding, padding, null);
		}
		if(isGameOver) {
			//System.out.println("Game over");
			//gameOverPanel.setVisible(true);
			winFrame winFrame = null;
			try {
				winFrame = new winFrame();
			} catch (IOException e) {
				e.printStackTrace();
			}
			timer.stop();
			g.setColor(Color.black);
			g.setFont(new Font(null, Font.BOLD, 30));
			g.drawString("You Win!", (this.getWidth()/2)-padding, padding-20);

			timer.stop();
			infoOfPlayer = "Time Taken: " + timer.minutes_string + "min" + timer.seconds_string + "sec";
			try {
				myWriter.write("-"+(timer.minutes*60+timer.seconds)+"-"+mouseClicks+"\n");
				myWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!timer.minutes_string.equals("00"))
				winFrame.add(new JLabel("Time Taken: " + timer.minutes_string + "min" + timer.seconds_string + "sec"));
			else winFrame.add(new JLabel("Time Taken: " + timer.seconds_string + "sec"));
			winFrame.add(new JLabel("Number of Moves: "+ mouseClicks));
             winFrame.setVisible(true);
		}

	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		slidingPuzzle puzzle = new slidingPuzzle();
	    MenuFrame menuFrame = new MenuFrame();
	    menuFrame.startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				puzzle.timer.start();
				puzzle.setVisible(true);
				try {
					puzzle.myWriter.write(menuFrame.tf.getText());
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}

				menuFrame.setVisible(false);
			}

		});

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	private boolean checkEmptyBox(int[] square) {
		int a,b;
		a=square[0];
		b=square[1];
		if (grid[a][b]==0)
			return true;
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

		if(isValidPoint(e.getPoint()))
			checkEmpty(getSquare(e.getPoint()));
		if (checkEmptyBox(getSquare((e.getPoint())))) {
			mouseClicks = mouseClicks + 1;
			mouseClicks_string = String.format("%03d", mouseClicks);
			moveLabel.setText("Moves: "+ mouseClicks_string);
		}

	}

	private boolean checkEmpty(int[] square) {
		// TODO Auto-generated method stub
		int a,b;
		a=square[0];
		b=square[1];
		int x=-2000;
		int y=x;
		boolean empty=false;
		if(a!=0) {
			if(grid[a-1][b]==0) {
				x=a-1;
				y=b;
				empty=true;
			}
		}
		if(b!=0&&!empty) {
			if(grid[a][b-1]==0) {
				x=a;
				y=b-1;
				empty=true;
			}
		}
		if(a!=grid.length-1&&!empty) {
			if(grid[a+1][b]==0) {
				x=a+1;
				y=b;
				empty=true;
			}
		}
		if(b!=grid[a].length-1) {
			if(grid[a][b+1]==0) {
				x=a;
				y=b+1;
				empty=true;
			}
		}
		if(empty) {
			moveSquare(a, b, x, y);}
		return empty;
	}

	private void moveSquare(int a,int b,int x,int y) {
		// TODO Auto-generated method stub
		grid[x][y]=grid[a][b];
		grid[a][b]=0;

		BufferedImage tempImage = imageArr[x][y];
		imageArr[x][y]=imageArr[a][b];
		imageArr[a][b]=tempImage;

		if(Arrays.deepEquals(grid, solvedState)) {
			isGameOver=true;
			showLines=false;
			showNumbers=false;
		}
		//System.out.println("current array: "+Arrays.toString(grid));
		//System.out.println("solved array: "+Arrays.toString(solvedState));
		updateImage();

	}

	private int[] getSquare(Point point) {
		// TODO Auto-generated method stub
		int[] RowNCol=new int[2];
		RowNCol[0]=(int)((point.getY()-padding)/height);
		RowNCol[1]=(int)((point.getX()-padding)/width);
		//		System.out.println("y: "+point.getY()+" x: "+point.getX());
		//		System.out.print("Row: "+RowNCol[0]);
		//		System.out.println(" Col: "+RowNCol[1]);
		return RowNCol;
	}

	private boolean isValidPoint(Point point) {
		// TODO Auto-generated method stub
		if(point.getX()>padding&&point.getX()<=padding+noOfSquares*width&&point.getY()>padding&&point.getY()<=padding+noOfSquares*height)
		{
			return true;
		}
		return false;
	}
	private void Shuffle() {
		//initGrid();
		initImageArr();
		isGameOver=false;
		int a;
		int b;
		BufferedImage tempImage;

		for(int i=0;i<grid.length;i++) {
			for (int j = 0; j < grid[i].length; j++) {
				int temp=grid[i][j];
				tempImage = imageArr[i][j];
				if(i==grid.length-1&&j==grid[i].length-1) {
					break;
				}
//				if(grid[i][j]==0) {
//					grid[i][j]=grid[grid.length-1][grid[i].length-1];
//					imageArr[i][j]=imageArr[imageArr.length-1][imageArr[i].length-1];
//					imageArr[imageArr.length-1][imageArr[i].length-1]=tempImage;
//					continue;
//				}
				do{ 
					a=(int)(Math.random()*grid.length);
					b=(int)(Math.random()*grid[i].length);
				}while(a==grid.length-1&&b==grid[i].length-1);

				grid[i][j]=grid[a][b];
				grid[a][b]=temp;

				imageArr[i][j]=imageArr[a][b];
				imageArr[a][b]=tempImage;

			}
		}
//		grid[grid.length-1][grid.length-1]=0;
		inversions=0;
		if(isSolvable()&&inversions<15)
			updateImage();
		else {
			Shuffle();
		}
	}

	private boolean isSolvable() {
		// TODO Auto-generated method stub
		//if(countInversions(convertMatrixtoArray())%2==(noOfSquares-1)%2)
		if(countInversions(convertMatrixtoArray())%2==0)
			return true;
		return false;



	}
	private int[] convertMatrixtoArray() {
		int[] arr=new int[(noOfSquares*noOfSquares)-1];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if((i*noOfSquares+j)>=arr.length)
					break;
				arr[i*noOfSquares+j]=grid[i][j];
			}
		}
		return arr;
	}
	private int countInversions(int[] arr) {
		int inv= countInversionsUtil(arr,0,arr.length-1);
		return inv;
	}

	private int countInversionsUtil(int[] arr, int l, int r) {
		// TODO Auto-generated method stub
		//System.out.print("l: "+l+" r: "+r+"| ");
		int count=0;
		if(l<r) {
			int m=(l+r)/2;
			count+=countInversionsUtil(arr, l, m);
			//System.out.println();
			count+=countInversionsUtil(arr, m+1, r);
			count+=merge(arr,l,m,r);
		}
		return count;
	}
	int merge(int arr[], int l, int m, int r)
	{
		int swaps=0;
		int n1 = m - l + 1;
		int n2 = r - m;

		int L[] = new int[n1];
		int R[] = new int[n2];

		for (int i = 0; i < n1; ++i)
			L[i] = arr[l + i];
		for (int j = 0; j < n2; ++j)
			R[j] = arr[m + 1 + j];

		int i = 0, j = 0;

		int k = l;
		while (i < n1 && j < n2) {
			if (L[i] <= R[j]) {
				arr[k] = L[i];
				i++;
			}
			else {
				//            	System.out.println();
				//            	System.out.println("L: "+Arrays.toString(L));
				//            	System.out.println("R: "+Arrays.toString(R));
				//            	System.out.println("i:"+i+" j:"+j+" k:"+k);
				//            	System.out.println("L[i]:"+L[i]+" R[j]:"+R[j]);
				//            	System.out.println("inv: "+(n1-i));
				swaps+=n1-i;
				arr[k] = R[j];
				j++;
			}
			k++;
		}

		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}

		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
		return swaps;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="Shuffle")
			Shuffle();
		if(e.getActionCommand()=="Toggle Lines") {
			showLines=!showLines;
			updateImage();
		}
		if(e.getActionCommand()=="Toggle Numbers") {
			showNumbers=!showNumbers;
			updateImage();
		}
		if(e.getActionCommand()=="Select Image") {
			fileChooser.showOpenDialog(this);
			imageFile=fileChooser.getSelectedFile();
			initBufferedImage();
		}

	}

}
