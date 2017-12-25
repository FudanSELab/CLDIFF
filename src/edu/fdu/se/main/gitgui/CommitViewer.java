package edu.fdu.se.main.gitgui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.fdu.se.config.ProjectProperties;
import edu.fdu.se.config.PropertyKeys;
import edu.fdu.se.git.JGitCommand;
import edu.fdu.se.git.JGitTagCommand;
import edu.fdu.se.gitrepo.RepoConstants;

public class CommitViewer {

	JGitCommand myCmd;
	private JFrame mainFrame;
	private JPanel controlPanel;
	private JButton jButton;
	private JTextField commitInput;

	public CommitViewer() {
		prepareGUI();
	}

	public static void main(String[] args) {
		CommitViewer swingContainerDemo = new CommitViewer();
		swingContainerDemo.upperPanel();
		swingContainerDemo.subPanel();
	}

	private void prepareGUI() {
		myCmd = new JGitTagCommand(ProjectProperties.getInstance().getValue(PropertyKeys.ANDROID_REPO_PATH2)
				+ RepoConstants.platform_frameworks_base_ + ".git");
		mainFrame = new JFrame("Commit Viewer");
		mainFrame.setSize(1024, 624);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}

	private void upperPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		FlowLayout layout = new FlowLayout();
		layout.setHgap(10);
		layout.setVgap(10);
		panel.setLayout(layout);
		panel.add(new JLabel(" Repository:/platform/frameworks/base/.git"));
		commitInput = new JTextField(54);
		panel.add(commitInput);
		jButton = new JButton("load");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String commitId = commitInput.getText();
				if(commitId.length()!=40){
					return;
				}
				JGitRepoManager.getInstance().parserCommit(commitId);
				commitIdIndexOfJList = JGitRepoManager.getInstance().updateDataModel(listModel);
				commitLogDetail.setText(JGitRepoManager.getInstance().commitInfoSummary());
			}
		});
		panel.add(jButton);
		panel.setPreferredSize(new Dimension(1000, 100));

		controlPanel.add(panel, BorderLayout.NORTH);
		mainFrame.setVisible(true);
	}
	/**
	 * tab1
	 */
	private JTextArea commitLogDetail;
	/**
	 * tab2
	 */
	private JEditorPane changedFileContent;
	JList<String> fileList;
	DefaultListModel<String> listModel;
	List<Integer> commitIdIndexOfJList;
	
	private void subPanel() {
		JTabbedPane jtabpane = new JTabbedPane();
		JPanel jp1 = new JPanel();
		commitLogDetail = new JTextArea("Commit msg text");
		commitLogDetail.setPreferredSize(new Dimension(980,400));
		jp1.add(commitLogDetail);
		//
		JPanel jp2 = new JPanel();
		jp2.setLayout(new BorderLayout());
		listModel = new DefaultListModel<String>();

		listModel.addElement("load your commit");

		fileList = new JList<String>(listModel);
		fileList.setPreferredSize(new Dimension(400,200));
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
            	if(fileList.getValueIsAdjusting()){
            		System.out.println("click");
            	}else{
            		int index = fileList.getSelectedIndex();
            		if(commitIdIndexOfJList.contains(index)){
            			System.out.println("contain. not do anything");
            		}else{
            			//TODO 设置editor内容
            			String filePath = fileList.getSelectedValue();
            			System.out.println(filePath);
            			
            			changedFileContent.setText("public\np\n\n\naaa");
            			
            		}
            		
            	}
            }

        });

		JScrollPane fileListPanel = new JScrollPane(fileList);
		changedFileContent = new JEditorPane();
		changedFileContent.setPreferredSize(new Dimension(600, 500));
		jp2.add(new JScrollPane(changedFileContent), BorderLayout.EAST);
		jp2.add(fileListPanel, BorderLayout.WEST);

		//

		jtabpane.addTab("Commit", jp1);
		jtabpane.addTab("Diff", jp2);

		jtabpane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		jtabpane.setPreferredSize(new Dimension(1000, 400));
		controlPanel.add(jtabpane, BorderLayout.SOUTH);
		mainFrame.setVisible(true);
	}



}