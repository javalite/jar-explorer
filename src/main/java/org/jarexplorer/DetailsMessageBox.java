package org.jarexplorer;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This is a class for displaying a dialog with a message on it. It also
 * displays a details text in the bottom pane if a "Details" button is depressed.
 * Here is how to use it:
 * <code>
 * general.DetailsMessageBox mb = new general.DetailsMessageBox(parent, "Warning",
 * "This is just a warning text \n second line of warning text",
 * "This is details text \n second line of text", JOptionPane.WARNING_MESSAGE);
 * mb.setVisible(true);
 * </code>
 * This dialog will be resized to it's optimal size each time a "Details" button is depressed.
 * The dialog is dismissed when "OK" button is depressed.
 *
 *
 * @author Igor Polevoy
 * Date: Jan 3, 2003
 * Time: 12:58:43 PM
 */
public class DetailsMessageBox extends CenteredDialog
{

    private JButton detailsB;
    private JButton okB;
    private boolean open;
    private int type;

    /**
     * Constructor
     *
     * @param parent - parent frame
     * @param title - title of dialog
     * @param message - message text to be displayed on the dialog
     * @param details - details text to be displayed if "Details" button is pressed
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */
    public DetailsMessageBox(Frame parent, String title, String message, String details, int type)
    {
        super(parent, title, true);
        this.type = type;
        init(message);
        addListeners(details);
    }

    /**
     * Constructor
     *
     * @param parent - parent dialog
     * @param title - title of dialog
     * @param message - message text to be displayed on the dialog
     * @param details - details text to be displayed if "Details" button is pressed
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */
    public DetailsMessageBox(Dialog parent, String title, String message, String details, int type)
    {
        super(parent, title, true);
        this.type = type;
        init(message);
        addListeners(details);
    }

    /**
     * initializes gui
     *
     * @param message - text message
     */
    private void init(String message)
    {
        detailsB = new JButton("Details >>");
        okB = new JButton("OK");

        //this is a panel that holds icon, text and the control panel with buttons
        JPanel messagePanel = new JPanel(new BorderLayout(20, 20));
        JTextArea ta = new JTextArea(message);
        ta.setEditable(false);
        ta.setBackground(messagePanel.getBackground());

        messagePanel.add(ta, BorderLayout.CENTER);
        messagePanel.add(new JLabel(" "), BorderLayout.NORTH);

        //get the specified icon from the UIDefaults
        Icon icon;
        if(JOptionPane.ERROR_MESSAGE == type)
        {
            icon = (Icon)UIManager.get("OptionPane.errorIcon");
        }
        else if(JOptionPane.INFORMATION_MESSAGE == type)
        {
            icon = (Icon)UIManager.get("OptionPane.informationIcon");
        }
        else if(JOptionPane.WARNING_MESSAGE == type)
        {
            icon = (Icon)UIManager.get("OptionPane.warningIcon");
        }
        else
        {
            throw new IllegalArgumentException("Incorrect type of message box: " + type + ". Correct types are: javax.swing.JOptionPane.WARNING_MESSAGE, javax.swing.JOptionPane.ERROR_MESSAGE, javax.swing.JOptionPane.INFORMATION_MESSAGE");
        }
        JLabel l = new JLabel(icon);
        //the following code is to more or less center the stupid icon.
        //setHorizontalAlignment() does not do much, apparently it only affects text
        l.setBorder(new Border()
        {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
            {
            }

            public Insets getBorderInsets(Component c)
            {
                return new Insets(10, 20, 10,10);
            }

            public boolean isBorderOpaque()
            {
                return false;
            }
        });
        messagePanel.add(l,BorderLayout.WEST);
        messagePanel.add(new JLabel(" "), BorderLayout.EAST);//this is just a margin on the right

        //controlPanel holds buttons and has a FlowLayout
        JPanel controlPanel = new JPanel();
        controlPanel.add(okB);
        controlPanel.add(detailsB);
        messagePanel.add(controlPanel, BorderLayout.SOUTH);
        getContentPane().setLayout(new BorderLayout());
        //messagePanel is in the CENTER, detailesPane gets added to the SOUTH of content pane
        getContentPane().add(messagePanel, BorderLayout.CENTER);

        pack();
    }

    /**
     * Adds listeners to buttons
     *
     * @param details - detals
     */
    private void addListeners(final String details)
    {
        detailsB.addActionListener(new ActionListener()
        {
            private JScrollPane pane;

            public void actionPerformed(ActionEvent e)
            {
                if(!open)
                {
                    JTextArea detailsTA = new JTextArea(details);
                    detailsTA.setEditable(false);
                    pane = new JScrollPane(detailsTA);
                    pane.setPreferredSize(new Dimension(100, 80));
                    getContentPane().add(pane, BorderLayout.SOUTH);
                    detailsB.setText("Details <<");
                    DetailsMessageBox.this.setSize(DetailsMessageBox.this.getSize().width,  DetailsMessageBox.this.getSize().height + 50);
                    DetailsMessageBox.this.pack();
                    open = true;
                }
                else
                {
                    getContentPane().remove(pane);
                    DetailsMessageBox.this.setSize(DetailsMessageBox.this.getSize().width,  DetailsMessageBox.this.getSize().height - 50);
                    detailsB.setText("Details >>");
                    DetailsMessageBox.this.pack();
                    open = false;
                }

            }
        });


        okB.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                DetailsMessageBox.this.dispose();
            }
        });

    }

    /**
     * Show message box
     *
     * @param parent - parent frame
     * @param title - title of dialog
     * @param message - message text to be displayed on the dialog
     * @param details - details text to be displayed if "Details" button is pressed
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */
    public static void showMessage(Frame parent, String title, String message, String details, int type)
    {
        DetailsMessageBox mb = new DetailsMessageBox(parent, "Warning", "This is just a warning text \n second line of warning text", "This is details text \n second line of text", JOptionPane.WARNING_MESSAGE);
        mb.setVisible(true);
    }
    /**
     * Show message box
     *
     * @param parent - parent dialog
     * @param title - title of dialog
     * @param message - message text to be displayed on the dialog
     * @param details - details text to be displayed if "Details" button is pressed
     * @param type - type of icon to display on the dialog. Possible options:
     * <code>
     * javax.swing.JOptionPane.WARNING_MESSAGE
     * javax.swing.JOptionPane.ERROR_MESSAGE
     * javax.swing.JOptionPane.INFORMATION_MESSAGE
     * </code>
     */
    public static void showMessage(Dialog parent, String title, String message, String details, int type)
    {
        DetailsMessageBox mb = new DetailsMessageBox(parent, "Warning", "This is just a warning text \n second line of warning text", "This is details text \n second line of text", JOptionPane.WARNING_MESSAGE);
        mb.setVisible(true);
    }



    /**
     * main method - for testing only
     *
     * @param args
     */
    public static void main(String [] args)
    {
        JFrame parent = new JFrame();
        DetailsMessageBox mb = new DetailsMessageBox(parent, "Warning", "This is just a warning text \n second line of warning text", "This is details text \n second line of text", JOptionPane.WARNING_MESSAGE);
        mb.setVisible(true);
    }
}
