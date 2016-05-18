package MathGUI;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WSFrame extends JFrame implements ActionListener{

	private static final int Width = 800;
	private static final int Height = 480;
	private static final int LINES = 10;
	private static final int CHAR_PER_LINE = 40;

	private JTextPane f;

	public WSFrame(){
		super();
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setSize(Width, Height);
		this.add(panel);

		f = new JTextPane();
		f.setEditable(false);
		f.setText("Main Menu");
		f.setLocation(100, 100);
		f.setBackground(Color.LIGHT_GRAY);
		panel.add(f);


		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());

		JMenu menu = new JMenu("Units");
		JMenuItem m;

		m = new JMenuItem("Unit 1");
		m.addActionListener(this);
		menu.add(m);

		m = new JMenuItem("Unit 2");
		m.addActionListener(this);
		menu.add(m);

		m = new JMenuItem("Unit 3");
		m.addActionListener(this);
		menu.add(m);

		m = new JMenuItem("Unit 4");
		m.addActionListener(this);
		menu.add(m);

		m = new JMenuItem("Unit 5");
		m.addActionListener(this);
		menu.add(m);

		m = new JMenuItem("Unit 6");
		m.addActionListener(this);
		menu.add(m);


		JMenuBar mBar = new JMenuBar();
		mBar.add(menu);
		setJMenuBar(mBar);

		//		theText = new JTextArea(LINES, CHAR_PER_LINE);
		//		theText.setBackground(Color.WHITE);
		//		this.add(theText);
	}

	public static void main(String[] args) {
		WSFrame frame = new WSFrame();
		frame.setSize(Width, Height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
		if(!f.getText().equals(ac)){
			f.setText(ac);
		}

	}




}
