/*
 * @(#)DirectionPanel.java	1.3 01/04/21
 *
 * Copyright (c) 1997-2001 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * @version 1.3 04/21/01
 * @author Jeff Dinkins
 * @author Chester Rose
 * @author Brian Beck
 */ 

public class DirectionPanel extends JPanel {

    private ButtonGroup group;

    public DirectionPanel(boolean enable, String selection, ActionListener l) {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	setAlignmentY(TOP_ALIGNMENT);
	setAlignmentX(LEFT_ALIGNMENT);

	Box firstThree = Box.createHorizontalBox();
	Box secondThree = Box.createHorizontalBox();
	Box thirdThree = Box.createHorizontalBox();

	if(!enable) {
	    selection = "None";
	}

        group = new ButtonGroup();
	DirectionButton b;
	b = (DirectionButton) firstThree.add(new DirectionButton(  tl_dot, tldn_dot, "NW", "Sets the orientation to the North-West", l, group, selection.equals("NW")));
        b.getAccessibleContext().setAccessibleName("North West");
	b.setEnabled(enable);
	b = (DirectionButton) firstThree.add(new DirectionButton(  tm_dot, tmdn_dot, "N",  "Sets the orientation to the North", l, group, selection.equals("N")));
        b.getAccessibleContext().setAccessibleName("North");
	b.setEnabled(enable);
	b = (DirectionButton) firstThree.add(new DirectionButton(  tr_dot, trdn_dot, "NE", "Sets the orientation to the North-East", l, group, selection.equals("NE")));
        b.getAccessibleContext().setAccessibleName("North East");
	b.setEnabled(enable);
	b = (DirectionButton) secondThree.add(new DirectionButton( ml_dot, mldn_dot, "W", "Sets the orientation to the West", l, group, selection.equals("W")));
        b.getAccessibleContext().setAccessibleName("West");
	b.setEnabled(enable);
	b = (DirectionButton) secondThree.add(new DirectionButton( c_dot,  cdn_dot,  "C", "Sets the orientation to the Center", l, group, selection.equals("C")));
        b.getAccessibleContext().setAccessibleName("Center");
	b.setEnabled(enable);
	b = (DirectionButton) secondThree.add(new DirectionButton( mr_dot, mrdn_dot, "E", "Sets the orientation to the East", l, group, selection.equals("E")));
        b.getAccessibleContext().setAccessibleName("East");
	b.setEnabled(enable);
	b = (DirectionButton) thirdThree.add(new DirectionButton(  bl_dot, bldn_dot, "SW", "Sets the orientation to the South-West", l, group, selection.equals("SW")));
        b.getAccessibleContext().setAccessibleName("South West");
	b.setEnabled(enable);
	b = (DirectionButton) thirdThree.add(new DirectionButton(  bm_dot, bmdn_dot, "S", "Sets the orientation to the South", l, group, selection.equals("S")));
        b.getAccessibleContext().setAccessibleName("South");
	b.setEnabled(enable);
	b = (DirectionButton) thirdThree.add(new DirectionButton(  br_dot, brdn_dot, "SE", "Sets the orientation to the South-East", l, group, selection.equals("SE")));
        b.getAccessibleContext().setAccessibleName("South East");
	b.setEnabled(enable);

	add(firstThree);
	add(secondThree);
	add(thirdThree);	
    }

    public String getSelection() {
        return group.getSelection().getActionCommand();
    }

    public void setSelection( String selection  ) {
        Enumeration e = group.getElements();
        while( e.hasMoreElements() ) {
            JRadioButton b = (JRadioButton)e.nextElement();
            if( b.getActionCommand().equals(selection) ) {
               b.setSelected(true);
            }
        }
    }
    
    // Chester's way cool layout buttons 
    public ImageIcon bl_dot   = loadImageIcon("bl.gif","bottom left layout button");
    public ImageIcon bldn_dot = loadImageIcon("bldn.gif","selected bottom left layout button");
    public ImageIcon bm_dot   = loadImageIcon("bm.gif","bottom middle layout button");
    public ImageIcon bmdn_dot = loadImageIcon("bmdn.gif","selected bottom middle layout button");
    public ImageIcon br_dot   = loadImageIcon("br.gif","bottom right layout button");
    public ImageIcon brdn_dot = loadImageIcon("brdn.gif","selected bottom right layout button");
    public ImageIcon c_dot    = loadImageIcon("c.gif","center layout button");
    public ImageIcon cdn_dot  = loadImageIcon("cdn.gif","selected center layout button");
    public ImageIcon ml_dot   = loadImageIcon("ml.gif","middle left layout button");
    public ImageIcon mldn_dot = loadImageIcon("mldn.gif","selected middle left layout button");
    public ImageIcon mr_dot   = loadImageIcon("mr.gif","middle right layout button");
    public ImageIcon mrdn_dot = loadImageIcon("mrdn.gif","selected middle right layout button");
    public ImageIcon tl_dot   = loadImageIcon("tl.gif","top left layout button");
    public ImageIcon tldn_dot = loadImageIcon("tldn.gif","selected top left layout button");
    public ImageIcon tm_dot   = loadImageIcon("tm.gif","top middle layout button");
    public ImageIcon tmdn_dot = loadImageIcon("tmdn.gif","selected top middle layout button");
    public ImageIcon tr_dot   = loadImageIcon("tr.gif","top right layout button");
    public ImageIcon trdn_dot = loadImageIcon("trdn.gif","selected top right layout button");
    
    public ImageIcon loadImageIcon(String filename, String description) {
	String path = "/resources/images/buttons/" + filename;
	return new ImageIcon(getClass().getResource(path), description);
    }

    
    public class DirectionButton extends JRadioButton {
        
        /**
         * A layout direction button
         */
        public DirectionButton(Icon icon, Icon downIcon, String direction,
                               String description, ActionListener l, 
                               ButtonGroup group, boolean selected)
        {
            super();
            this.addActionListener(l);
            setFocusPainted(false);
            setHorizontalTextPosition(CENTER);
            group.add(this);
            setIcon(icon);
            setSelectedIcon(downIcon);
            setActionCommand(direction);
            getAccessibleContext().setAccessibleName(direction);
            getAccessibleContext().setAccessibleDescription(description);
            setSelected(selected);
        }

        public boolean isFocusTraversable() {
            return false;
        }

        public void setBorder(Border b) {
        }
    }
}