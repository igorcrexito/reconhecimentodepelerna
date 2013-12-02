/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import org.neuroph.core.NeuralNetwork;
import utils.Utils;

/**
 *
 * @author Igor
 */
public class CameraCapture {

    int i = 0;
    PlanarImage imagem;

    static {
        Webcam.setHandleTermSignal(true);
    }

    public void captureCam(JanelaPrincipal painelPrincipal, NeuralNetwork redeNeural) throws IOException, InterruptedException {
        BufferedImage image;
        
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320, 240));
        int i = 0;
        webcam.open(true);

        //displayImageWebcam();
        
        ImageTransformer ite = new ImageTransformer(redeNeural,0);
       /* while (true) {
            image = webcam.getImage();

            imagem = PlanarImage.wrapRenderedImage(image);
            PlanarImage reconhecida = Utils.reconheceImagem(imagem, redeNeural);
            ImageIO.write(reconhecida, "PNG", new File("C:/snapsTirados/reconhecida" + i + ".png"));
            ImageIO.write(imagem, "PNG", new File("C:/snapsTirados/reconhecid" + i + ".png"));
            i++;
        }*/
        
        
        //webcam.close();
        //return imagem;
    }

    private void displayImageWebcam() {
        JFrame window = new JFrame("Test Webcam Panel");
        window.add(new WebcamPanel(Webcam.getDefault()));
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void takeASnapShot() {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(320, 240));

        if (!webcam.isOpen())
        webcam.open();
        BufferedImage image = webcam.getImage();
        webcam.close();
        try {
            ImageIO.write(image, "PNG", new File("C:/snapsTirados/" + i + ".png"));
            i++;
        } catch (IOException ex) {
            Logger.getLogger(CameraCapture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
