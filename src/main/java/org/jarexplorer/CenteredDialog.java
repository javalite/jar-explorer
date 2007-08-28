package org.jarexplorer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 *   Subclasses of this clas will be displayed in the center of the screen.   The dialog will be disposed if
 * 'Escape' button is closed.
 *   @author Igor Polevoy
 */
public class CenteredDialog extends JDialog
{
    /**
     *  Constructor
     * @param   owner  owner frame
     * @param   title  title text - will be diaplayed on the title bar
     * @param   modal  true if modal, false otherwise
     */
    public CenteredDialog(Frame owner, String title, boolean modal)
    {
        super(owner, title, modal);
        Action escape = new AbstractAction()
        {
            public void actionPerformed(java.awt.event.ActionEvent arg1)
            {
                dispose();
            }
        };
        ((JComponent)getContentPane()).registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ((JComponent)getContentPane()).registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_FOCUSED);
    }

    /**
     *  Constructor
     * @param   owner  owner dialog
     * @param   title  title text - will be diaplayed on the title bar
     * @param   modal  true if modal, false otherwise
     */
    public CenteredDialog(Dialog owner, String title, boolean modal)
    {
        super(owner, title, modal);
        Action escape = new AbstractAction()
        {
            public void actionPerformed(java.awt.event.ActionEvent arg1)
            {
                dispose();
            }
        };
        ((JComponent)getContentPane()).registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ((JComponent)getContentPane()).registerKeyboardAction(escape, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_FOCUSED);
    }


    /**
     *  Overrides Component.setVisible(). Contains code for centering.
     * @param   arg1  true to display , false fo hide
     */
    public void setVisible(boolean arg1)
    {
        GUIUtil.centerWindow(this);
        super.setVisible(arg1);
    }
}
