import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.Font;


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
}
