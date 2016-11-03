package json;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class Project_GUI extends JFrame implements ActionListener{
	private JToolBar toolBar,menuBar;
	private JButton bEnter;
	private JTextField textField;
	private JPanel pMain;
	private JScrollPane scrollPane;
	Vector columnNames;
	Vector data;
	public Project_GUI(){
		setTitle("Demo Big Data.");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.white);
		setSize(1024, 700);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		columnNames = new Vector();
		data = new Vector();
		
		//  thanh chon chuong trinh, o tren
		{
			toolBar = new JToolBar();
			toolBar.setLayout(new FlowLayout());
			toolBar.setFloatable(false);
			
			toolBar.add(textField = new JTextField("http://environment.data.gov.uk/flood-monitoring/id/stations"));
			toolBar.add(bEnter = new JButton("Run")); 
			bEnter.setPreferredSize(new Dimension(70,30));
			bEnter.setBackground(Color.lightGray);
			bEnter.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  Json_Process a=new Json_Process();
		    	  try {
					a.runJson();
					LoadTable();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		      }
		    });
			
			JPanel pnMenu = new JPanel(new BorderLayout());
			pnMenu.add(toolBar, BorderLayout.CENTER);
			
			add(pnMenu, BorderLayout.NORTH);
		}
		//Menu Bar
		{
			menuBar = new JToolBar();
			menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width,35));
			menuBar.setBackground(Color.white);
			JLabel cr = new JLabel("  Copyright Duy Tan University  ");
			menuBar.add(cr,BorderLayout.EAST);
			add(menuBar,BorderLayout.SOUTH);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {	
	}
	public void LoadTable()
	{
		String hostName = "localhost";
	     String sid = "RJAVAGROUP";
	     String userName = "rjgroup";
	     String password = "rjgroup123";
		String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
		try {
			Connection dbConn = DriverManager.getConnection(connectionURL, userName,password);
			String sql="select * from TREE";
			try (Statement stmt = dbConn.createStatement(); 
			        ResultSet rs = stmt.executeQuery(sql)) {
			            ResultSetMetaData md = rs.getMetaData();
			            int columns = md.getColumnCount();
			            //  Get column names
			            for (int i = 1; i <= columns; i++) {
			                columnNames.addElement(md.getColumnName(i));
			            }
			            //  Get row data
			            while (rs.next()) {
			                Vector row = new Vector(columns);
			                for (int i = 1; i <= columns; i++) {
			                    row.addElement(rs.getObject(i));
			                }
			                data.addElement(row);
			            }
			        }
					dbConn.close();
			    } catch (Exception e) {
			    }
				JTable table = new JTable(data, columnNames) {
		        @Override
		        public Class getColumnClass(int column) {
		            for (int row = 0; row < getRowCount(); row++) {
		                Object o = getValueAt(row, column);
		                if (o != null) {
		                    return o.getClass();
		                }
		            }
		            return Object.class;
		        }
				};
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				for(int i=0;i<table.getColumnCount();i++)
				{
					table.getColumnModel().getColumn(i).setPreferredWidth(100);
				}
				scrollPane = new JScrollPane(table);
				add(scrollPane, BorderLayout.CENTER);
		}
	
	public static void main(String[] args) {
		Project_GUI t=new Project_GUI();
		t.setVisible(true);;
	}
}
