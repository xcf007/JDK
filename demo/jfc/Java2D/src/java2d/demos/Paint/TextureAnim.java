/*
 * @(#)TextureAnim.java	1.13 01/12/03
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java2d.demos.Paint;
    
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import javax.swing.*;
import java2d.AnimatingControlsSurface;
import java2d.CustomControls;



/**
 * TexturePaint animation with controls for transformations.
 */
public class TextureAnim extends AnimatingControlsSurface {

    public static final Color colorblend = new Color(0f, 0f, 1f, .5f);
    protected static BufferedImage textureImg;
    protected int bNum;
    protected int tilesize;
    private boolean newtexture;
    private TexturePaint texture;
    private Rectangle tilerect;
    private boolean bouncesize = false;
    private boolean bouncerect = true;
    private boolean rotate = false;
    private boolean shearx = false;
    private boolean sheary = false;
    private boolean showanchor = true;
    private boolean quality = false;
    private AnimVal w, h, x, y, rot, shx, shy;
    private static Image img[] = new Image[2];
    

    public TextureAnim() {
        img[0] = getImage("duke.gif");   // 8 bit gif
        img[1] = getImage("duke.png");   // 24 bit png

        textureImg = makeImage(32, 0);
        tilesize = textureImg.getWidth();
        w = new AnimVal(0, 200, 3, 10, tilesize);
        h = new AnimVal(0, 200, 3, 10, tilesize);
        x = new AnimVal(0, 200, 3, 10, 0);
        y = new AnimVal(0, 200, 3, 10, 0);
        rot = new AnimVal(-360, 360, 5, 15, 0);
        shx = new AnimVal(-50, 50, 3, 10, 0);
        shy = new AnimVal(-50, 50, 3, 10, 0);
        tilerect = new Rectangle(x.getInt(), y.getInt(),
                                 w.getInt(), h.getInt());
        texture = new TexturePaint(textureImg, tilerect);
        setControls(new Component[] { new DemoControls(this) });
    }


    protected BufferedImage makeImage(int size, int num) {
        newtexture = true;
        switch (bNum = num) {
            case 0 : return makeRGBImage(size);
            case 1 : return makeGIFImage(size);
            case 2 : return makePNGImage(size);
        }
        return null;
    }


    private BufferedImage makeRGBImage(int size) {
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        big.setColor(Color.white);
        big.fillRect(0, 0, size, size);
        for (int j = 0; j < size; j++) {
            float red = j / (float) size;
            for (int i = 0; i < size; i++) {
                float green = i / (float) size;
                big.setColor(new Color(1.0f - red, 1.0f - green, 0.0f, 1.0f));
                big.drawLine(i, j, i, j);
            }
        }
        return bi;
    }


    private BufferedImage makeGIFImage(int d) {
        BufferedImage bi = new BufferedImage(d, d, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        big.drawImage(img[0], 0, 0, d, d, new Color(204, 204, 255), null);
        return bi;
    }


    private BufferedImage makePNGImage(int d) {
        BufferedImage bi = new BufferedImage(d, d, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = bi.createGraphics();
        big.drawImage(img[1], 0, 0, d, d, Color.lightGray, null);
        return bi;
    }


    public void reset(int width, int height) {
        x.newlimits(-width/4, width/4 - w.getInt());
        y.newlimits(-height/4, height/4 - h.getInt());
    }


    public void step(int width, int height) {
        if (tilesize != textureImg.getWidth()) {
            tilesize = textureImg.getWidth();
        }
        if (bouncesize) {
            w.anim();
            h.anim();
            x.newlimits(-width/4, width/4 - w.getInt());
            y.newlimits(-height/4, height/4 - h.getInt());
        } else {
            if (w.getInt() != tilesize) {
                w.set(tilesize);
                x.newlimits(-width/4, width/4 - w.getInt());
            }
            if (h.getInt() != tilesize) {
                h.set(tilesize);
                y.newlimits(-height/4, height/4 - h.getInt());
            }
        }
        if (bouncerect) {
            x.anim();
            y.anim();
        }
        if (newtexture ||
            x.getInt() != tilerect.x || y.getInt() != tilerect.y ||
            w.getInt() != tilerect.width || h.getInt() != tilerect.height)
        {
            newtexture = false;
            int X = x.getInt();
            int Y = y.getInt();
            int W = w.getInt();
            int H = h.getInt();
            tilerect = new Rectangle(X, Y, W, H);
            texture = new TexturePaint(textureImg, tilerect);
        }
    }


    public void render(int width, int height, Graphics2D g2) {

        g2.translate(width/2, height/2);
        if (rotate) {
            rot.anim();
            g2.rotate(Math.toRadians(rot.getFlt()));
        } else {
            rot.set(0);
        }
        if (shearx) {
            shx.anim();
            g2.shear(shx.getFlt()/100, 0.0f);
        } else {
            shx.set(0);
        }
        if (sheary) {
            shy.anim();
            g2.shear(0.0f, shy.getFlt()/100);
        } else {
            shy.set(0);
        }
        g2.setPaint(texture);
        g2.fillRect(-1000, -1000, 2000, 2000);
        if (showanchor) {
            g2.setColor(Color.black);
            g2.setColor(colorblend);
            g2.fill(tilerect);
        }
    }


    public static void main(String argv[]) {
        createDemoFrame(new TextureAnim());
    }


    static class AnimVal {
        float curval;
        float lowval;
        float highval;
        float currate;
        float lowrate;
        float highrate;

        public AnimVal(int lowval, int highval,
                       int lowrate, int highrate) {
            this.lowval = lowval;
            this.highval = highval;
            this.lowrate = lowrate;
            this.highrate = highrate;
            this.curval = randval(lowval, highval);
            this.currate = randval(lowrate, highrate);
        }

        public AnimVal(int lowval, int highval,
                       int lowrate, int highrate,
                       int pos) {
            this(lowval, highval, lowrate, highrate);
            set(pos);
        }

        public float randval(float low, float high) {
            return (float) (low + Math.random() * (high - low));
        }

        public float getFlt() {
            return curval;
        }

        public int getInt() {
            return (int) curval;
        }

        public void anim() {
            curval += currate;
            clip();
        }

        public void set(float val) {
            curval = val;
            clip();
        }

        public void clip() {
            if (curval > highval) {
                curval = highval - (curval - highval);
                if (curval < lowval) {
                    curval = highval;
                }
                currate = - randval(lowrate, highrate);
            } else if (curval < lowval) {
                curval = lowval + (lowval - curval);
                if (curval > highval) {
                    curval = lowval;
                }
                currate = randval(lowrate, highrate);
            }
        }

        public void newlimits(int lowval, int highval) {
            this.lowval = lowval;
            this.highval = highval;
            clip();
        }
    }  // End AnimVal class


    class DemoControls extends CustomControls implements ActionListener {

        TextureAnim demo;
        JToolBar toolbar;
        JComboBox combo;
        JMenu menu;
        JMenuItem menuitems[];
        int iconSize = 20;
        

        public DemoControls(TextureAnim demo) {
            super(demo.name);
            this.demo = demo;
            menuitems = new JMenuItem[3];
            setBackground(Color.gray);
            add(toolbar = new JToolBar());
            toolbar.setFloatable(false);
            addTool("BO", "bounce", true);
            addTool("SA", "show anchor", true);
            addTool("RS", "resize", false);
            addTool("RO", "rotate", false);
            addTool("SX", "shear x", false);
            addTool("SY", "shear y", false);
            add(combo = new JComboBox());
            combo.addActionListener(this);
            combo.addItem("8");
            combo.addItem("16");
            combo.addItem("32");
            combo.addItem("64");
            combo.addItem("80");
            combo.setSelectedIndex(2);

            JMenuBar menuBar = new JMenuBar();
            menu = (JMenu) menuBar.add(new JMenu());
            for (int i = 0; i < 3; i++) {
                BufferedImage bimg = demo.makeImage(iconSize, i);
                TexturedIcon icon = new TexturedIcon(bimg);
                menuitems[i] = menu.add(new JMenuItem(icon));
                menuitems[i].addActionListener(this);
            } 
            menu.setIcon(menuitems[0].getIcon());
            add(menuBar);
           demo.bNum = 0;
        }


        public void addTool(String str, String toolTip, boolean state) {
            JButton b = (JButton) toolbar.add(new JButton(str));
            b.setBackground(state ? Color.green : Color.lightGray);
            b.setSelected(state);
            b.setToolTipText(toolTip);
            b.addActionListener(this);
        }


        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();
            if (obj instanceof JComboBox) {
		String selItem = (String) combo.getSelectedItem();
		if (selItem != null) {
		    int size = Integer.parseInt(selItem);
		    demo.textureImg = demo.makeImage(size, demo.bNum);
		}
            } else if (obj instanceof JMenuItem) {
                for (int i = 0; i < menuitems.length; i++) {
                    if (obj.equals(menuitems[i])) {
                        demo.textureImg = demo.makeImage(demo.tilesize, i);
                        menu.setIcon(menuitems[i].getIcon());
                        break;
                    } 
                }
            } else {
                JButton b = (JButton) obj;
                b.setSelected(!b.isSelected());
                b.setBackground(b.isSelected() ? Color.green : Color.lightGray);
                if (b.getText().equals("BO")) {
                    demo.bouncerect = b.isSelected();
                } else if (b.getText().equals("SA")) {
                    demo.showanchor = b.isSelected();
                } else if (b.getText().equals("RS")) {
                    demo.bouncesize = b.isSelected();
                } else if (b.getText().equals("RO")) {
                    demo.rotate = b.isSelected();
                } else if (b.getText().equals("SX")) {
                    demo.shearx = b.isSelected();
                } else if (b.getText().equals("SY")) {
                    demo.sheary = b.isSelected();
                }
            }
            if (demo.animating.thread == null) {
                demo.repaint();
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(200,37);
        }


        public void run() {
            Thread me = Thread.currentThread();
            while (thread == me) {
                for (int i = 2; i < toolbar.getComponentCount(); i++) {
                    try {
                        thread.sleep(4444);
                    } catch (InterruptedException e) { return; }
                    ((JButton) toolbar.getComponentAtIndex(i)).doClick();
                }
            }
            thread = null;
        }


        class TexturedIcon implements Icon {
            BufferedImage bi;
            public TexturedIcon(BufferedImage bi) {
                this.bi = bi;
            }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                Rectangle r = new Rectangle(x, y, iconSize, iconSize);
                g2.setPaint(new TexturePaint(bi, r));
	        g2.fillRect(x, y, iconSize, iconSize);
	        g2.setColor(Color.gray);
	        g2.draw3DRect(x, y, iconSize-1, iconSize-1, true);
            }
            public int getIconWidth() { return iconSize; }
            public int getIconHeight() { return iconSize; }
        } // End TexturedIcon class
    } // End DemoControls class
} // End TextureAnim class