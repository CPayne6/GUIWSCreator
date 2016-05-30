import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.apache.pdfbox.pdmodel.*;

import java.awt.Font;
import java.util.Arrays;


public class MathFrame extends JFrame implements TreeSelectionListener {

	private JTree tree;
	private DefaultMutableTreeNode selectedNode;
	JTextPane textPane = new JTextPane();
	JButton btnGenerate = new JButton("Generate");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MathFrame window = new MathFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MathFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		this.setBounds(100, 100, 450, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		
		btnGenerate.setBounds(26, 205, 79, 29);
		this.getContentPane().add(btnGenerate);
		btnGenerate.setEnabled(false);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(87, 171, 45, 28);
		spinner.setModel(new SpinnerNumberModel(1,1,99,1));
		
		this.getContentPane().add(spinner);
		JLabel lblOfCopies = new JLabel("# of copies");
		lblOfCopies.setBounds(16, 177, 86, 16);
		this.getContentPane().add(lblOfCopies);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(121, 27, -107, 140);
		this.getContentPane().add(scrollPane);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 27, 132, 140);
		this.getContentPane().add(scrollPane_1);
		initializeTree();
		tree.setRootVisible(false);
		scrollPane_1.setViewportView(tree);
		JTextArea textArea = new JTextArea();
		textArea.setBounds(209, 27, -34, 29);
		this.getContentPane().add(textArea);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(215, 243, 159, 29);
		this.getContentPane().add(btnExport);


		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(144, 28, 294, 206);
		this.getContentPane().add(scrollPane_2);
		textPane.setEditable(false);
		scrollPane_2.setViewportView(textPane);

		JLabel lblUnits = new JLabel("Units:");
		lblUnits.setBounds(6, 6, 61, 16);
		this.getContentPane().add(lblUnits);

		JLabel lblPreview = new JLabel("Preview:");
		lblPreview.setBounds(150, 6, 61, 16);
		this.getContentPane().add(lblPreview);
	}

	private void initializeTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Math Dojo");
		for (int i=0;i<5;i++) {
			DefaultMutableTreeNode unit = new DefaultMutableTreeNode("Unit "+(i+1));
			unit.add(new DefaultMutableTreeNode("Belt 1"));
			unit.add(new DefaultMutableTreeNode("Belt 2"));
			unit.add(new DefaultMutableTreeNode("Belt 3"));
			unit.add(new DefaultMutableTreeNode("Belt 4"));
			top.add(unit);
		}
		tree=new JTree(top);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
	}

	public void valueChanged(TreeSelectionEvent e) {
		selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (selectedNode == null) return;
		if (!selectedNode.isLeaf()&&btnGenerate.isEnabled()) {
			btnGenerate.setEnabled(false);
			System.out.println("disable");
		}
		else if ((selectedNode.isLeaf()&&!btnGenerate.isEnabled())){
			btnGenerate.setEnabled(true);
			System.out.println("enable");
		}
	}
	
	
	
	//PDF Handling
	
	private static void createWorksheets(String[] worksheets,String[] answers,String unit,String colour) {

		String[] serials=new String[worksheets.length];
		PDDocument doc = new PDDocument();
		try {
			for (int i=0;i<worksheets.length;i++) {

				PDPage page=new PDPage();
				PDFont font = PDType1Font.HELVETICA;
				doc.addPage(page);
				PDPageContentStream content = new PDPageContentStream(doc, page);
				content.beginText();

				content.setFont( PDType1Font.HELVETICA, 12 );
				content.newLineAtOffset( 50, 730 );
				content.showText("Name: _____________________________");

				content.setFont( PDType1Font.HELVETICA_BOLD, 18 );
				content.newLineAtOffset( 100, 0 );
				content.showText(unit);
				content.newLineAtOffset( 0, -25 );
				content.showText(colour+" Belt");
				
				content.setFont( PDType1Font.HELVETICA, 8 );
				content.newLineAtOffset( -100,-10 );
				serials[i]=generateSerial();
				content.showText("SERIAL NUMBER:   ");
				content.setFont( PDType1Font.HELVETICA_BOLD, 8 );
				content.showText(serials[i]);


				content.setFont( font, 12 );
				content.newLineAtOffset( 40, -35 );
				String[] lines=getLines(worksheets[i]);

				for (int p=0;p<lines.length&&p<30;p++) {
					content.newLineAtOffset( 0,-20 );
					content.showText(lines[p]);
				}
				content.endText();
				content.close();

				if (lines.length>30) {
					page=new PDPage();
					doc.addPage(page);
					content = new PDPageContentStream(doc, page);
					content.beginText();
					content.setFont( font, 12 );
					content.newLineAtOffset( 90,695 );
					for (int p=30;p<lines.length&&p<60;p++) {
						content.newLineAtOffset( 0,-20 );
						content.showText(lines[p]);
					}
					content.endText();
					content.close();
				}
				
				
				
			}

			//answers
			PDPage page=new PDPage();
			doc.addPage(page);
			PDPageContentStream content = new PDPageContentStream(doc, page);
			content.beginText();

			content.setFont( PDType1Font.HELVETICA_BOLD, 18 );
			content.newLineAtOffset( 50, 725 );
			content.showText("ANSWER KEY");

			content.setFont( PDType1Font.HELVETICA, 10 );
			content.newLineAtOffset( 40, -15 );
			for (int i=0;i<worksheets.length;i++) {
				content.newLineAtOffset( 0, -20 );
				content.showText("Worksheet ");
				content.setFont( PDType1Font.HELVETICA_BOLD, 10 );
				content.showText(serials[i]);
				content.setFont( PDType1Font.HELVETICA, 10 );
				
				String[] lines=getLines(answers[1]);
				for (int p=0;p<lines.length&&p<3;p++) {
					content.newLineAtOffset( 0,-10 );
					content.showText(lines[p]);
				}
			}
			
			content.endText();
			content.close();

			doc.save(unit+" Worksheets.pdf");
			doc.close();
		}
		catch (Exception io){
			System.err.println(io);
		}
	}
	private static String generateSerial() {
		String serial="";
		for (int i=0;i<4;i++) {
			int digit=(int)(Math.random()*36);
			if (digit<10) {
				serial+=digit;
			}
			else {
				serial+=(char)(digit+55);
			}
		}
		return serial;
	}
	private static String[] getLines(String s) {
		String[] lines=new String[1];
		while (s.indexOf('\n')!=-1) {
			lines[lines.length-1]=s.substring(0,s.indexOf('\n'));
			s=s.substring(s.indexOf('\n')+1);
			lines=Arrays.copyOf(lines,lines.length+1);
		}
		lines[lines.length-1]=s;
		return lines;
	}
}
