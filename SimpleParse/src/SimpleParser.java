import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.eugeneyche.seleniumparser.Action;
import com.eugeneyche.seleniumparser.Command;
import com.eugeneyche.seleniumparser.Parser;

public class SimpleParser extends JFrame {
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new SimpleParser();


	}
	
	private static final long serialVersionUID = 1L;

	public SimpleParser() {
		this.setSize(600, 600);
		this.setTitle("Simple Parser");
		final MainPanel main = new MainPanel();
		setContentPane(main);
		new FileDrop(this, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] arg0) {
				for(int i = 0; i < arg0.length; i++) {
					try {
						main.topPanel.selectedFilePath.setText(arg0[0].getPath());
					} catch(Exception e) {
						
					}
				}
			}
			
		});
		
		setVisible(true);
	}
	
	public class MainPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		Action action;
		Command command;
		TopPanel topPanel;
		WestPanel westPanel;
		CenterPanel centerPanel;
		BottomPanel bottomPanel;
		
		public MainPanel() {
			super(new BorderLayout());
			add(topPanel = new TopPanel(), BorderLayout.PAGE_START);
			add(westPanel = new WestPanel(), BorderLayout.WEST);
			add(centerPanel = new CenterPanel(), BorderLayout.CENTER);
			add(bottomPanel = new BottomPanel(), BorderLayout.PAGE_END);
		}
		
		public void updateAll() {
			if(action != null) {
				int index = (westPanel.commands.getSelectedIndex());
				if(0 <= index && index < action.commands.size())
					command = action.commands.get(index);
			}
			westPanel.update();
			bottomPanel.selectedFilePath.setText(topPanel.selectedFilePath.getText() + ".json");
			centerPanel.update();
		}
		
		
		
		public class TopPanel extends JPanel {
			
			private static final long serialVersionUID = 1L;
			JTextField selectedFilePath;
			
			public TopPanel() {
				super(new BorderLayout());
				selectedFilePath = new JTextField();
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Import"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			selectedFilePath.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					final JFileChooser fc = new JFileChooser();
					fc.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {}
						
					});
					try {
						fc.setCurrentDirectory(new File(selectedFilePath.getText()));
					} catch(Exception e) {}
					int returnVal = fc.showOpenDialog(selectedFilePath);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						selectedFilePath.setText(file.getPath());
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
			add(selectedFilePath);
			JButton parse = new JButton("Import");
			parse.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					try {
						action = Parser.parseSeleniumHTML(new FileInputStream(new File(selectedFilePath.getText())));
						command = null;
						MainPanel.this.updateAll();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			});
			add(parse, BorderLayout.EAST);
			}
		}
		
		public class BottomPanel extends JPanel {
			
			private static final long serialVersionUID = 1L;
			JTextField selectedFilePath;
			
			public BottomPanel() {
				super(new BorderLayout());
				selectedFilePath = new JTextField();
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Export"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			selectedFilePath.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					final JFileChooser fc = new JFileChooser();
					fc.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {}
						
					});
					try {
						fc.setCurrentDirectory(new File(selectedFilePath.getText()));
					} catch(Exception e) {}
					int returnVal = fc.showOpenDialog(selectedFilePath);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						selectedFilePath.setText(file.getPath());
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {}

				@Override
				public void mouseExited(MouseEvent arg0) {}

				@Override
				public void mousePressed(MouseEvent arg0) {}

				@Override
				public void mouseReleased(MouseEvent arg0) {}
				
			});
			add(selectedFilePath);
			JButton parse = new JButton("Export");
			final JComponent poot = this; 
			parse.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					try {
						PrintWriter out = new PrintWriter(new FileWriter(new File(selectedFilePath.getText())));
						out.println(Parser.convertActionToJson(action));
						JOptionPane.showMessageDialog(poot, "File Successfully Exported");
						out.close();
					} catch(Exception e) {
						JOptionPane.showMessageDialog(poot, "Failed to Export File");
						e.printStackTrace();
					}
				}
				
			});
			add(parse, BorderLayout.EAST);
			}
		}
		
		public class WestPanel extends JPanel {
			JTextField type;
			JTextField object;
			JTextField trigger;
			JList<Command> commands;
			private static final long serialVersionUID = 1L;
			public WestPanel() {
				super(new BorderLayout());
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Action"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
				JPanel summaryPanel = new JPanel();
				summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
				{
					JLabel label = new JLabel("Type ");
					label.setPreferredSize(new Dimension(50, getHeight()));
					type = new JTextField(10); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(type, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
				}
				{
					JLabel label = new JLabel("Object ");
					label.setPreferredSize(new Dimension(50, getHeight()));
					object = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(object, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
				}
				{
					JLabel label = new JLabel("Trigger");
					label.setPreferredSize(new Dimension(50, getHeight()));
					trigger = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(trigger, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
					summaryPanel.add(Box.createRigidArea(new Dimension(10, 10)));
					
					
				}
				add(summaryPanel, BorderLayout.PAGE_START);
				JPanel commandsPanel = new JPanel(new BorderLayout());
				commandsPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Commands"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
				commands = new JList<Command>();
				commands.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				commands.setFocusable(true);
				commands.setVerifyInputWhenFocusTarget(true);
				commands.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						if(action != null) {
							updateAll();
						}
					}
					
				});
				commandsPanel.add(new JScrollPane(commands));
				commands.setPreferredSize(new Dimension(commands.getWidth(), commands.getHeight()));
				add(commandsPanel);
				type.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(action != null)
							action.type = type.getText();
					}
					
				});
				object.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(action != null)
							action.object = object.getText();
					}
					
				});
				trigger.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(action != null)
							action.trigger = trigger.getText();
					}
					
				});
				
			}
			
			public void update() {
				if(action != null) {
					type.setText(action.type);
					object.setText(action.object);
					trigger.setText(action.trigger);
					commands.clearSelection();
					Vector<Command> data = new Vector<Command>();
					data.addAll(action.commands);
					commands.setListData(data);
				}
			}
		}
		
		
		
		public class CenterPanel extends JPanel {
			JTextField name;
			JTextField target;
			JTextField value;
			JTextField v_name;
			JTextField v_target;
			JTextField v_value;
			private static final long serialVersionUID = 1L;
			public CenterPanel() {
				super(new BorderLayout());
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Command"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));

				JPanel summaryPanel = new JPanel();
				summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
				{
					JLabel label = new JLabel("Name");
					label.setPreferredSize(new Dimension(50, getHeight()));
					name = new JTextField(10); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(name, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
				}
				{
					JLabel label = new JLabel("Target");
					label.setPreferredSize(new Dimension(50, getHeight()));
					target = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(target, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
				}
				{
					JLabel label = new JLabel("Value");
					label.setPreferredSize(new Dimension(50, getHeight()));
					value = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(value, BorderLayout.CENTER);
					summaryPanel.add(temp, BorderLayout.PAGE_START);
					summaryPanel.add(Box.createRigidArea(new Dimension(10, 10)));
				}
				JPanel v_summaryPanel = new JPanel();
				v_summaryPanel.setLayout(new BoxLayout(v_summaryPanel, BoxLayout.Y_AXIS));
				{
					JLabel label = new JLabel("Name");
					label.setPreferredSize(new Dimension(50, getHeight()));
					v_name = new JTextField(10); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(v_name, BorderLayout.CENTER);
					v_summaryPanel.add(temp);
				}
				{
					JLabel label = new JLabel("Target");
					label.setPreferredSize(new Dimension(50, getHeight()));
					v_target = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(v_target, BorderLayout.CENTER);
					v_summaryPanel.add(temp);
				}
				{
					JLabel label = new JLabel("Value");
					label.setPreferredSize(new Dimension(50, getHeight()));
					v_value = new JTextField(); 
					JPanel temp = new JPanel(new BorderLayout());
					temp.add(label, BorderLayout.WEST);
					temp.add(v_value, BorderLayout.CENTER);
					v_summaryPanel.add(temp);
				}
				v_summaryPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder("Verification Command"), 
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
				summaryPanel.add(v_summaryPanel);
				add(summaryPanel, BorderLayout.PAGE_START);
				name.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null)
							command.command = name.getText();
					}
					
				});
				target.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null)
							command.target = target.getText();
					}
					
				});
				value.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null)
							command.value = value.getText();
					}
					
				});
				v_name.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null) {
							command.setVCommand(v_name.getText(), v_target.getText(), v_value.getText());
						}
							
					}
					
				});
				v_target.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null) {
							command.setVCommand(v_name.getText(), v_target.getText(), v_value.getText());
						}	
					}
					
				});
				v_value.getDocument().addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						warn();
						
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						warn();
					}

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						warn();
					}
					
					public void warn() {
						if(command != null) {
							command.setVCommand(v_name.getText(), v_target.getText(), v_value.getText());
						}
							
					}
					
				});
				
			}
			
			public void update() {
				if(command != null) {
					name.setText(command.command);
					target.setText(command.target);
					value.setText(command.value);
					if(command.v_command != null) {
						v_name.setText(command.v_command.command);
						v_target.setText(command.v_command.target);
						v_value.setText(command.v_command.value);
					} else {
						v_name.setText("");
						v_target.setText("");
						v_value.setText("");
					}
				} else {
					name.setText("");
					target.setText("");
					value.setText("");
					v_name.setText("");
					v_target.setText("");
					v_value.setText("");
				}
			}
		}
	}
}
