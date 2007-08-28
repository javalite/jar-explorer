package org.jarexplorer;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JLabel;


/**
 * Options Dialog. Not much here... 
 * 
 * @author igor.jarexplorer
 * 
 */
public class OptionsDialog extends CenteredDialog
{

     public OptionsDialog(Frame owner)
     {
         super(owner, "Options", true);
         setSize(new Dimension(400, 300));
         getContentPane().add(new JLabel("Options tbd..."));
     }
}
