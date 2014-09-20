/*
 * DebugToolBar.java
 *
 * Created on April 17, 2007, 8:46 PM
 */

package scs.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 *
 * @author Matt Mehne
 */

public class DebugToolBar extends JToolBar{
    
    private JButton nextStepButton;
    private JButton addWatchButton;
    private JButton clearAllButton;
    public JTextField numCycleField;
    public boolean cycleflag = true;
    
    private SchematicPanel schemPanel;
    
    /**
     * Creates a new instance of DebugToolBar
     */
    public DebugToolBar(SchematicPanel sp) {
        schemPanel = sp;
        
        nextStepButton = new JButton("Step");
        addWatchButton = new JButton("Add Watch");
        clearAllButton = new JButton("Clear All");
        
        this.setOrientation(JToolBar.HORIZONTAL);
        
        numCycleField = new JTextField(3);
        numCycleField.setMaximumSize(new Dimension(50,19));
        numCycleField.setMinimumSize(new Dimension(40,10));
        numCycleField.setText("1");
        numCycleField.setToolTipText("Number of cycles per step");
        
        numCycleField.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                cycleflag = true;
            }

            public void keyReleased(KeyEvent e) {
            }
            
        });
        
        
        ActionListener buttonListener = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == nextStepButton)
                {
                    if(cycleflag)
                    {
                        cycleflag = false;
                        String s = numCycleField.getText();
                        s = s.trim();
                        if(s.indexOf('.') >= 0 || (s.indexOf('0') == 0 && s.length() > 1))
                        {
                            numCycleField.setText("1");
                            JOptionPane.showMessageDialog(null, "Incorrect step value " + s + ". Must be positive integer.", "Oops", JOptionPane.ERROR_MESSAGE);
                            schemPanel.debugDoNext();
                            return;
                        }
                        
                        int x = 1;
                        try{
                            x = Integer.parseInt(s);
                        }
                        catch(NumberFormatException ex)
                        {
                            JOptionPane.showMessageDialog(null, ex, "Must input proper integer", JOptionPane.ERROR_MESSAGE);
                            numCycleField.setText("  1");
                        }
                        schemPanel.setNumCycles(x);
                        
                    }
                    schemPanel.debugDoNext();
                }
                else if(e.getSource() == addWatchButton)
                {
                    schemPanel.debugAddWatch();
                }
                else if(e.getSource() == clearAllButton)
                {
                    schemPanel.debugClearAll();
                }
                
            }
            
        };
        
        nextStepButton.addActionListener(buttonListener);
        addWatchButton.addActionListener(buttonListener);
        clearAllButton.addActionListener(buttonListener);
        //this.setLayout(new FlowLayout());
        this.add(addWatchButton);
        this.add(nextStepButton);
        this.add(numCycleField);
        this.add(clearAllButton);
    }
    
    
    
}
