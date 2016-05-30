/***
 * JOJO READ THIS:
 * 
 * I found a super easy way to add ActionListener to buttons or anything really.
 * All you have to do is add a listener to the button (button.addActionListener) 
 * through a method reference. Pretty much what you have to do is write a method
 * that you want to execute when the button is pressed and then pass it to the
 * addActionListener method (I wrote the exact syntax below). I added an example of this below where the generate
 * button is initialized. I wrote the method generatePressed and passed it to the
 * button. This way it is much easier to handle each button as we can define a 
 * method for each. (I also changed the TreeListener to work this way)
 * 
 * Syntax for this method reference: buttonObject.addActionListener(this::methodYouWrite);
 * Make sure the method you write has one ActionEvent parameter and returns void.
 */



import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;


public class MathFrame extends JFrame{

	private JTree tree;
	private DefaultMutableTreeNode selectedNode;
	private JTextPane textPane = new JTextPane();
	private JScrollPane scrollPane_2 = new JScrollPane();
	private JButton btnGenerate = new JButton("Generate");
	
	private PDDocument doc;

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
		
		//
		btnGenerate.addActionListener(this::generatePressed); //adds a listener to the button that calls the generatePressed method
		//
		
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
		tree.addTreeSelectionListener(this::selectBelt);
	}

	
	/**
	 * CALLED WHEN A NODE ON THE TREE IS SELECTED
	 * @param e
	 */
	public void selectBelt(TreeSelectionEvent e) {
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
	
	/**
	 * CALLED WHEN THE "Generate" BUTTON IS PRESSED
	 * @param e
	 */
	private void generatePressed(ActionEvent e) {
		String unit="";
		String belt="";
		String[] questions={""};
		String[] answers={""};
		doc=createWorksheets(questions,answers,unit,belt);
		scrollPane_2.setViewportView(pageToLabel(doc,1));
	}
	
	
	//PDF Handling
	
	private static PDDocument createWorksheets(String[] worksheets,String[] answers,String unit,String colour) {

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

				content.setFont( PDType1Font.HELVETICA_BOLD, 24 );
				content.newLineAtOffset( 350, 10 );
				content.showText(unit);
				content.newLineAtOffset( 0, -25 );
				content.showText(colour+" Belt");
				
				content.setFont( PDType1Font.HELVETICA, 8 );
				content.newLineAtOffset( -350, 0 );
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
		return doc;
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
	
	private static JLabel pageToLabel(PDDocument pdf,int page) {
		JLabel label=null;
		try {
			label = new JLabel(new ImageIcon((new PDFRenderer(pdf)).renderImage(page)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return label;
	}
	
}
