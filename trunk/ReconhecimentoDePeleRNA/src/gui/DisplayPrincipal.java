/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.swing.JPanel;

/**
 *
 * @author Igor
 */
public class DisplayPrincipal extends JPanel {

    private PlanarImage image;

    public DisplayPrincipal(PlanarImage image) {
        super();
        setImage(image);
    }

    public DisplayPrincipal() {
        super();
    }

    public void setImage(PlanarImage image) {
        setSize(600, 600);
        //image = scale((RenderedOp) image,(float)600/(float)image.getWidth(),(float)600/(float)image.getHeight());
        this.image = image;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image!=null)
            g.drawImage(image.getAsBufferedImage(), 0, 0, getWidth(), getHeight(), this);

    }

    public PlanarImage getImage() {
        return image;
    }
    
    private static RenderedOp scale(RenderedOp image, float scaleH, float scaleV) {
        ParameterBlock scaleParams = new ParameterBlock();
        scaleParams.addSource(image);
        scaleParams.add(scaleH).add(scaleV).add(0.0f).add(0.0f);
        scaleParams.add(Interpolation.getInstance(Interpolation.INTERP_BICUBIC_2));

        RenderingHints scalingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        scalingHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        scalingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        scalingHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        scalingHints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        scalingHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        scalingHints.put(JAI.KEY_BORDER_EXTENDER, BorderExtender.createInstance(BorderExtender.BORDER_COPY));

        return JAI.create("scale", scaleParams, scalingHints);
 }

    
    
}
