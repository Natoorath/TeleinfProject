package errordetection;

// Autor: Piotr Pańczyk

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ErrorDetection extends JFrame
{
	SpringLayout transmitterLayout = new SpringLayout();
	SpringLayout receiverLayout = new SpringLayout();
	SpringLayout summaryLayout = new SpringLayout();
	
	JPanel codeSelectionPanel = new JPanel(new FlowLayout());
	JPanel crcSelectionPanel = new JPanel(new FlowLayout());
	JPanel transmitterPanel = new JPanel(transmitterLayout);
	JPanel receiverPanel = new JPanel(receiverLayout);
	JPanel summaryPanel = new JPanel(summaryLayout);

	Border raisedbevel = BorderFactory.createRaisedBevelBorder();

	JRadioButton parityRadioButton = new JRadioButton("Bit parzystości");
	JRadioButton hammingRadioButton = new JRadioButton("Kodowanie Hamminga");
	JRadioButton crcRadioButton = new JRadioButton("CRC");
	//JRadioButton crc32RadioButton = new JRadioButton("CRC-32");
	ButtonGroup codeButtonGroup = new ButtonGroup();
	ButtonGroup crcButtonGroup = new ButtonGroup();

	JLabel inputDataLabel = new JLabel("Surowe");
	JTextPane inputData = new JTextPane();
	JLabel transmitterDataLabel = new JLabel("Zakodowane");
	JTextPane transmitterData = new JTextPane();
	JButton generateButton = new JButton("Generuj");
	JSpinner generateNumberSpinner = new JSpinner(new SpinnerNumberModel(32, 8, 1024, 8));
	JButton encodeButton = new JButton("Koduj");

	JLabel outputDataLabel = new JLabel("Surowe");
	JTextPane outputData = new JTextPane();
	JLabel receivedDataLabel = new JLabel("Zakodowane");
	JTextPane receivedData = new JTextPane();
	JLabel correctedDataLabel = new JLabel("Zakodowane po korekcji");
	JTextPane correctedData = new JTextPane();
	JButton interfereButton = new JButton("Zakłóć");
	JSpinner interferencesNumberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1024, 1));

	JButton decodeButton = new JButton("Dekoduj");
	JButton clearButton = new JButton("Wyczyść");

	JLabel dataBitsLabel = new JLabel("Przesłane bity danych");
	JTextPane dataBits = new JTextPane();
	JLabel controlBitsLabel = new JLabel("Przesłane bity nadmiarowe");
	JTextPane controlBits = new JTextPane();
	JLabel detectedErrorsLabel = new JLabel("Błędy wykryte");
	JTextPane detectedErrors = new JTextPane();
	JLabel fixedErrorsLabel = new JLabel("Błędy skorygowane");
	JTextPane fixedErrors = new JTextPane();
	JLabel notDetectedErrorsLabel = new JLabel("Błędy niewykryte");
	JTextPane notDetectedErrors = new JTextPane();

	DataBits inputBits = new DataBits();
	
	Parity parityTransmitter = new Parity();
	Hamming hammingTransmitter = new Hamming();
	Crc crcTransmitter = new Crc();
	//Crc32 crc32Transmitter = new Crc32();
	Parity parityReceiver = new Parity();
	Hamming hammingReceiver = new Hamming();
	Crc crcReceiver = new Crc();
	//Crc32 crc32Receiver = new Crc32();
	CodeBase transmitter=parityTransmitter;
	CodeBase receiver=parityReceiver;
	
	JRadioButton atmRadioButton = new JRadioButton("ATM");
	JRadioButton crc12RadioButton = new JRadioButton("CRC-12");
	JRadioButton crc16RadioButton=new JRadioButton("CRC-16");
	JRadioButton crc16revRadioButton=new JRadioButton("CRC-16 REVERSE");
	JRadioButton sdlcRadioButton=new JRadioButton("SDLC");
	JRadioButton sdlcRevRadioButton=new JRadioButton("SDLC REVERSE");
	JRadioButton crc32RadioButton=new JRadioButton("CRC-32");
	
	ErrorDetection()
	{
		//setLayout(new GridLayout(5, 1));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		/*codeSelectionPanel.setLayout(new FlowLayout());
		crcSelectionPanel.setLayout(new FlowLayout());
		transmitterPanel.setLayout(new FlowLayout());
		receiverPanel.setLayout(new FlowLayout());
		summaryPanel.setLayout(new FlowLayout());*/

		codeSelectionPanel.setBorder(BorderFactory.createTitledBorder(raisedbevel, "Metoda detekcji/korekcji"));
		crcSelectionPanel.setBorder(BorderFactory.createTitledBorder(raisedbevel, "Rodzaj CRC"));
		transmitterPanel.setBorder(BorderFactory.createTitledBorder(raisedbevel, "Nadawca"));
		receiverPanel.setBorder(BorderFactory.createTitledBorder(raisedbevel, "Odbiorca"));
		summaryPanel.setBorder(BorderFactory.createTitledBorder(raisedbevel, "Podsumowanie"));

		codeSelectionPanel.setMaximumSize(new Dimension(800, 50));
		crcSelectionPanel.setMaximumSize(new Dimension(800, 50));
		transmitterPanel.setMaximumSize(new Dimension(800, 140));
		receiverPanel.setMaximumSize(new Dimension(800, 170));
		summaryPanel.setMaximumSize(new Dimension(800, 100));
		
		add(codeSelectionPanel);
		add(crcSelectionPanel);
		add(transmitterPanel);
		add(receiverPanel);
		add(summaryPanel);

		codeButtonGroup.add(parityRadioButton);
		codeButtonGroup.add(hammingRadioButton);
		codeButtonGroup.add(crcRadioButton);
		//codeButtonGroup.add(crc32RadioButton);
		
		crcButtonGroup.add(atmRadioButton);
		crcButtonGroup.add(crc12RadioButton);
		crcButtonGroup.add(crc16RadioButton);
		crcButtonGroup.add(crc16revRadioButton);
		crcButtonGroup.add(sdlcRadioButton);
		crcButtonGroup.add(sdlcRevRadioButton);
		crcButtonGroup.add(crc32RadioButton);
		
		codeSelectionPanel.add(parityRadioButton);
		codeSelectionPanel.add(hammingRadioButton);
		codeSelectionPanel.add(crcRadioButton);
		//codeSelectionPanel.add(crc32RadioButton);
		
		crcSelectionPanel.add(atmRadioButton);
		crcSelectionPanel.add(crc12RadioButton);
		crcSelectionPanel.add(crc16RadioButton);
		crcSelectionPanel.add(crc16revRadioButton);
		crcSelectionPanel.add(sdlcRadioButton);
		crcSelectionPanel.add(sdlcRevRadioButton);
		crcSelectionPanel.add(crc32RadioButton);

		transmitterPanel.add(inputDataLabel);
		transmitterPanel.add(inputData);
		transmitterPanel.add(generateNumberSpinner);
		transmitterPanel.add(generateButton);
		transmitterPanel.add(transmitterDataLabel);
		transmitterPanel.add(transmitterData);
		transmitterPanel.add(encodeButton);

		transmitterLayout.putConstraint(SpringLayout.NORTH, inputDataLabel, 10, SpringLayout.NORTH, transmitterPanel);
		transmitterLayout.putConstraint(SpringLayout.NORTH, inputData, 10, SpringLayout.NORTH, transmitterPanel);
		
		transmitterLayout.putConstraint(SpringLayout.NORTH, generateNumberSpinner, 10, SpringLayout.SOUTH, inputData);
		transmitterLayout.putConstraint(SpringLayout.NORTH, generateButton, 10, SpringLayout.SOUTH, inputData);
		transmitterLayout.putConstraint(SpringLayout.NORTH, encodeButton, 10, SpringLayout.SOUTH, inputData);
		
		transmitterLayout.putConstraint(SpringLayout.NORTH, transmitterDataLabel, 10, SpringLayout.SOUTH, generateButton);
		transmitterLayout.putConstraint(SpringLayout.NORTH, transmitterData, 10, SpringLayout.SOUTH, generateButton);
		
		transmitterLayout.putConstraint(SpringLayout.WEST, inputDataLabel, 5, SpringLayout.WEST, transmitterPanel);
		transmitterLayout.putConstraint(SpringLayout.WEST, transmitterDataLabel, 5, SpringLayout.WEST, transmitterPanel);
		
		transmitterLayout.putConstraint(SpringLayout.WEST, generateNumberSpinner, 20, SpringLayout.WEST, inputData);
		transmitterLayout.putConstraint(SpringLayout.WEST, generateButton, 5, SpringLayout.EAST, generateNumberSpinner);
		transmitterLayout.putConstraint(SpringLayout.WEST, encodeButton, 5, SpringLayout.EAST, generateButton);
		
		transmitterLayout.putConstraint(SpringLayout.WEST, inputData, 190, SpringLayout.WEST, transmitterPanel);
		transmitterLayout.putConstraint(SpringLayout.WEST, transmitterData, 0, SpringLayout.WEST, inputData);

		inputData.setPreferredSize(new Dimension(582,19));
		transmitterData.setPreferredSize(new Dimension(582,19));
		receivedData.setPreferredSize(new Dimension(582,19));
		correctedData.setPreferredSize(new Dimension(582,19));
		outputData.setPreferredSize(new Dimension(582,19));
		dataBits.setPreferredSize(new Dimension(50,19));
		controlBits.setPreferredSize(new Dimension(50,19));
		detectedErrors.setPreferredSize(new Dimension(50,19));
		fixedErrors.setPreferredSize(new Dimension(50,19));
		notDetectedErrors.setPreferredSize(new Dimension(50,19));
		
		receiverPanel.add(receivedDataLabel);
		receiverPanel.add(receivedData);
		receiverPanel.add(interferencesNumberSpinner);
		receiverPanel.add(interfereButton);
		receiverPanel.add(decodeButton);
		receiverPanel.add(correctedDataLabel);
		receiverPanel.add(correctedData);
		receiverPanel.add(outputDataLabel);
		receiverPanel.add(outputData);
		
		receiverLayout.putConstraint(SpringLayout.NORTH, receivedDataLabel, 10, SpringLayout.NORTH, receiverPanel);
		receiverLayout.putConstraint(SpringLayout.NORTH, receivedData, 10, SpringLayout.NORTH, receiverPanel);
		
		receiverLayout.putConstraint(SpringLayout.NORTH, correctedDataLabel, 10, SpringLayout.SOUTH, decodeButton);
		receiverLayout.putConstraint(SpringLayout.NORTH, correctedData, 10, SpringLayout.SOUTH, decodeButton);
		
		receiverLayout.putConstraint(SpringLayout.NORTH, outputDataLabel, 10, SpringLayout.SOUTH, correctedData);
		receiverLayout.putConstraint(SpringLayout.NORTH, outputData, 10, SpringLayout.SOUTH, correctedData);
		
		receiverLayout.putConstraint(SpringLayout.WEST, receivedDataLabel, 5, SpringLayout.WEST, receiverPanel);
		receiverLayout.putConstraint(SpringLayout.WEST, correctedDataLabel, 5, SpringLayout.WEST, receiverPanel);
		receiverLayout.putConstraint(SpringLayout.WEST, outputDataLabel, 5, SpringLayout.WEST, receiverPanel);
		
		receiverLayout.putConstraint(SpringLayout.WEST, receivedData, 190, SpringLayout.WEST, receiverPanel);
		receiverLayout.putConstraint(SpringLayout.WEST, correctedData, 190, SpringLayout.WEST, receiverPanel);
		receiverLayout.putConstraint(SpringLayout.WEST, outputData, 190, SpringLayout.WEST, receiverPanel);
		
		receiverLayout.putConstraint(SpringLayout.NORTH, interferencesNumberSpinner, 10, SpringLayout.SOUTH, receivedData);
		receiverLayout.putConstraint(SpringLayout.NORTH, interfereButton, 10, SpringLayout.SOUTH, receivedData);
		receiverLayout.putConstraint(SpringLayout.NORTH, decodeButton, 10, SpringLayout.SOUTH, receivedData);
		
		receiverLayout.putConstraint(SpringLayout.WEST, interferencesNumberSpinner, 20, SpringLayout.WEST, receivedData);
		receiverLayout.putConstraint(SpringLayout.WEST, interfereButton, 5, SpringLayout.EAST, interferencesNumberSpinner);
		receiverLayout.putConstraint(SpringLayout.WEST, decodeButton, 5, SpringLayout.EAST, interfereButton);
		
		summaryPanel.add(dataBitsLabel);
		summaryPanel.add(dataBits);
		summaryPanel.add(controlBitsLabel);
		summaryPanel.add(controlBits);
		summaryPanel.add(clearButton);
		summaryPanel.add(detectedErrorsLabel);
		summaryPanel.add(detectedErrors);
		summaryPanel.add(fixedErrorsLabel);
		summaryPanel.add(fixedErrors);
		summaryPanel.add(notDetectedErrorsLabel);
		summaryPanel.add(notDetectedErrors);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, dataBitsLabel, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.NORTH, dataBits, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.NORTH, controlBitsLabel, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.NORTH, controlBits, 10, SpringLayout.NORTH, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.NORTH, clearButton, 10, SpringLayout.NORTH, summaryPanel);
		
		summaryLayout.putConstraint(SpringLayout.NORTH, detectedErrorsLabel, 10, SpringLayout.SOUTH, dataBits);
		summaryLayout.putConstraint(SpringLayout.NORTH, detectedErrors, 10, SpringLayout.SOUTH, dataBits);
		summaryLayout.putConstraint(SpringLayout.NORTH, fixedErrorsLabel, 10, SpringLayout.SOUTH, dataBits);
		summaryLayout.putConstraint(SpringLayout.NORTH, fixedErrors, 10, SpringLayout.SOUTH, dataBits);
		summaryLayout.putConstraint(SpringLayout.NORTH, notDetectedErrorsLabel, 10, SpringLayout.SOUTH, dataBits);
		summaryLayout.putConstraint(SpringLayout.NORTH, notDetectedErrors, 10, SpringLayout.SOUTH, dataBits);
		
		summaryLayout.putConstraint(SpringLayout.WEST, dataBitsLabel, 5, SpringLayout.WEST, summaryPanel);
		summaryLayout.putConstraint(SpringLayout.WEST, detectedErrorsLabel, 5, SpringLayout.WEST, summaryPanel);

		summaryLayout.putConstraint(SpringLayout.WEST, dataBits, 5, SpringLayout.EAST, dataBitsLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, detectedErrors, 5, SpringLayout.EAST, dataBitsLabel);
		
		summaryLayout.putConstraint(SpringLayout.WEST, controlBitsLabel, 40, SpringLayout.EAST, dataBits);
		summaryLayout.putConstraint(SpringLayout.WEST, fixedErrorsLabel, 40, SpringLayout.EAST, dataBits);
		
		summaryLayout.putConstraint(SpringLayout.WEST, controlBits, 5, SpringLayout.EAST, controlBitsLabel);
		summaryLayout.putConstraint(SpringLayout.WEST, fixedErrors, 5, SpringLayout.EAST, controlBitsLabel);
		
		summaryLayout.putConstraint(SpringLayout.WEST, notDetectedErrorsLabel, 40, SpringLayout.EAST, fixedErrors);
		summaryLayout.putConstraint(SpringLayout.WEST, notDetectedErrors, 5, SpringLayout.EAST, notDetectedErrorsLabel);
		
		summaryLayout.putConstraint(SpringLayout.EAST, clearButton, 0, SpringLayout.EAST, notDetectedErrors);
		
		transmitterData.setEditable(false);
		receivedData.setEditable(false);
		correctedData.setEditable(false);
		outputData.setEditable(false);
		dataBits.setEditable(false);
		controlBits.setEditable(false);
		detectedErrors.setEditable(false);
		fixedErrors.setEditable(false);
		notDetectedErrors.setEditable(false);

		parityRadioButton.setSelected(true);
		parityRadioButton.addItemListener(new RadioButtonSelection());
		hammingRadioButton.addItemListener(new RadioButtonSelection());
		crcRadioButton.addItemListener(new RadioButtonSelection());
		//crc32RadioButton.addItemListener(new RadioButtonSelection());
		atmRadioButton.addItemListener(new RadioButtonSelection());
		crc12RadioButton.addItemListener(new RadioButtonSelection());
		crc16RadioButton.addItemListener(new RadioButtonSelection());
		crc16revRadioButton.addItemListener(new RadioButtonSelection());
		sdlcRadioButton.addItemListener(new RadioButtonSelection());
		sdlcRevRadioButton.addItemListener(new RadioButtonSelection());
		crc32RadioButton.addItemListener(new RadioButtonSelection());
		
		crc16RadioButton.setSelected(true);
		activateCrc(false);
		((JSpinner.DefaultEditor)generateNumberSpinner.getEditor()).getTextField().setColumns(3);
		((JSpinner.DefaultEditor)interferencesNumberSpinner.getEditor()).getTextField().setColumns(3);
		
		encodeButton.setEnabled(false);
		interfereButton.setEnabled(false);
		decodeButton.setEnabled(false);
		
		generateButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				int n=Integer.parseInt(generateNumberSpinner.getValue().toString());
				if (n%8!=0)
				{
					n+=8-n%8;
					generateNumberSpinner.setValue(n);
				}
				inputBits.generate(n);
				inputData.setText(inputBits.toString());
			}
		});
		
		encodeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				String str = inputData.getText();
				if (str.length()==0)
				{
					str = "00000000";
					inputData.setText(str);
				}
				else if (str.length()%8!=0)
				{
					String temp = "";
					int zeros = 8-str.length()%8;
					for (int i=0; i<zeros; i++) temp+="0";
					str = temp + str;
					inputData.setText(str);
				}
				
				transmitter.setData(str);
				transmitter.encode();
				transmitterData.setText(transmitter.codeToString());
				transmitter.setCode(transmitter.codeToString());
				receivedData.setText(transmitter.codeToString());
			}
		});
		
		receivedData.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent me)
			{
				if (receivedData.getText().length()>0)
				{
					Point pt = new Point(me.getX(), me.getY());
					int pos = receivedData.viewToModel(pt);
					transmitter.negate(pos);
					receivedData.setText(transmitter.codeToString());
				}
			}
		});
		
		interfereButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				transmitter.interfere(Integer.parseInt(interferencesNumberSpinner.getValue().toString()));
				receivedData.setText(transmitter.codeToString());
			}
		});
		
		decodeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				transmitter.setCode(receivedData.getText());
				int errors=countErrors();
				transmitter.fix();
				correctedData.setText(transmitter.codeToString());
				colorFixedBits(transmitter.getBitTypes());
				transmitter.decode();
				outputData.setText(transmitter.dataToString());
				dataBits.setText(Integer.toString(transmitter.getDataBitsNumber()));
				controlBits.setText(Integer.toString(transmitter.getControlBitsNumber()));
				int detected = transmitter.getDetectedErrorsNumber();
				detectedErrors.setText(Integer.toString(detected));
				fixedErrors.setText(Integer.toString(transmitter.getFixedErrorsNumber()));
				notDetectedErrors.setText(Integer.toString(errors-detected));
			}
		});
		
		clearButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent ae)
			{
				inputData.setText("");
				transmitterData.setText("");
				receivedData.setText("");
				correctedData.setText("");
				outputData.setText("");
				dataBits.setText("");
				controlBits.setText("");
				detectedErrors.setText("");
				fixedErrors.setText("");
				notDetectedErrors.setText("");
				parityTransmitter = new Parity();
				hammingTransmitter = new Hamming();
				crcTransmitter = new Crc();
				//crc32Transmitter = new Crc32();
				parityReceiver = new Parity();
				hammingReceiver = new Hamming();
				crcReceiver = new Crc();
				//crc32Receiver = new Crc32();
			}
		});
		
		inputData.getDocument().addDocumentListener(new DocumentListener()
		{
			private void check()
			{
				if (inputData.getText().length()==0)
				{
					encodeButton.setEnabled(false);
				}
				else
				{
					encodeButton.setEnabled(true);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent de)
			{
				check();
			}

			@Override
			public void removeUpdate(DocumentEvent de)
			{
				check();
			}

			@Override
			public void changedUpdate(DocumentEvent de)
			{
				check();
			}
		});
		
		receivedData.getDocument().addDocumentListener(new DocumentListener()
		{
			private void check()
			{
				if (receivedData.getText().length()==0)
				{
					interfereButton.setEnabled(false);
					decodeButton.setEnabled(false);
				}
				else
				{
					interfereButton.setEnabled(true);
					decodeButton.setEnabled(true);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent de)
			{
				check();
			}

			@Override
			public void removeUpdate(DocumentEvent de)
			{
				check();
			}

			@Override
			public void changedUpdate(DocumentEvent de)
			{
				check();
			}
		});
		
		setTitle("Kodowanie");
		setSize(800, 535);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void clearCode()
	{
		transmitterData.setText("");
		receivedData.setText("");
		correctedData.setText("");
		outputData.setText("");
		dataBits.setText("");
		controlBits.setText("");
		detectedErrors.setText("");
		fixedErrors.setText("");
		notDetectedErrors.setText("");
	}
	
	private void activateCrc(boolean active)
	{
		atmRadioButton.setEnabled(active);
		crc12RadioButton.setEnabled(active);
		crc16RadioButton.setEnabled(active);
		crc16revRadioButton.setEnabled(active);
		sdlcRadioButton.setEnabled(active);
		sdlcRevRadioButton.setEnabled(active);
		crc32RadioButton.setEnabled(active);
	}
	
	private class RadioButtonSelection implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent ie)
		{
			if (ie.getStateChange()==ItemEvent.SELECTED)
			{
				JRadioButton button = (JRadioButton)ie.getItemSelectable();
				if (button==parityRadioButton)
				{
					transmitter=parityTransmitter;
					transmitter=parityReceiver;
					activateCrc(false);
				}
				else if (button==hammingRadioButton){
					transmitter=hammingTransmitter;
					transmitter=hammingReceiver;
					activateCrc(false);
				}
				else if (button==crcRadioButton)
				{
					transmitter=crcTransmitter;
					transmitter=crcReceiver;
					activateCrc(true);
				}
				/*else if (button==crc32RadioButton)
				{
					transmitter=crcRadioButton;
					transmitter=ocrc32RadioButton;
				}*/
				else if (button==atmRadioButton)
				{
					crcTransmitter.setKey(Crc.ATM);
					crcReceiver.setKey(Crc.ATM);
				}
				else if (button==crc12RadioButton)
				{
					crcTransmitter.setKey(Crc.CRC12);
					crcReceiver.setKey(Crc.CRC12);
				}
				else if (button==crc16RadioButton)
				{
					crcTransmitter.setKey(Crc.CRC16);
					crcReceiver.setKey(Crc.CRC16);
				}
				else if (button==crc16revRadioButton)
				{
					crcTransmitter.setKey(Crc.CRC16_REVERSE);
					crcReceiver.setKey(Crc.CRC16_REVERSE);
				}
				else if (button==sdlcRadioButton)
				{
					crcTransmitter.setKey(Crc.SDLC);
					crcReceiver.setKey(Crc.SDLC);
				}
				else if (button==sdlcRevRadioButton)
				{
					crcTransmitter.setKey(Crc.SDLC_REVERSE);
					crcReceiver.setKey(Crc.SDLC_REVERSE);
				}
				else if (button==crc32RadioButton)
				{
					crcTransmitter.setKey(Crc.CRC32);
					crcReceiver.setKey(Crc.CRC32);
				}
				clearCode();
			}
		}	
	}
	
	private int countErrors()
	{
		String input = transmitterData.getText();
		String output = receivedData.getText();
		if (input.length()!=output.length()) return -1;
		else
		{
			int errors=0;
			int l=input.length();
			for (int i=0; i<l; i++)
			{
				if (input.charAt(i)!=output.charAt(i)) errors++;
			}
			return errors;
		}
	}
	
	public void colorFixedBits(int type[])
	{
		String str=correctedData.getText();
		if (str.length()==type.length)
		{
			StyledDocument doc = correctedData.getStyledDocument();
			Style style = correctedData.addStyle("colours", null);
			
			correctedData.setText("");
			int l = type.length;
			for (int i=0; i<l; i++)
			{
				Color colour=Color.black;
				switch (type[i])
				{
					case 0: colour=Color.green; break;
					case 1: colour=Color.red; break;
					case 2: colour=Color.orange; break;
					case 3: colour=Color.cyan; break;
					case 4: colour=Color.magenta; break;
					case 5: colour=Color.yellow; break;
				}
				StyleConstants.setForeground(style, colour);
				try { doc.insertString(doc.getLength(), ""+str.charAt(i), style); }
				catch (BadLocationException e){}
			}
		}
	}
	
	public static void main(String[] args)
	{
		ErrorDetection errorDetection = new ErrorDetection();
	}
}
