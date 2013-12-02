/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JFrame;
import org.neuroph.core.NeuralNetwork;
import skinsegmentation.SkinDetection;
import utils.FeatureAssembly;

/**
 *
 * @author Igor
 */
public class ImageTransformer implements WebcamImageTransformer {

    NeuralNetwork redeNeural;
    private int algoritmo;

    public ImageTransformer(NeuralNetwork redeNeural, int algoritmo) {

        this.algoritmo = algoritmo;
        this.redeNeural = redeNeural;
        Webcam webcam = Webcam.getDefault();
        //webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.setImageTransformer(this);
        webcam.open();

        JFrame window = new JFrame("Pele Reconhecida");

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setFillArea(true);

        window.add(panel);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public BufferedImage transform(BufferedImage original) {
        PlanarImage imagemReconhecida = PlanarImage.wrapRenderedImage(original);

        int[] pixelImagemOriginal = new int[3];
        int[] pixelResultante = new int[3];

        double[] vetorFeatures = new double[9];

        Raster inputRaster = imagemReconhecida.getData();

        WritableRaster outputRaster = inputRaster.createCompatibleWritableRaster();
        RandomIter iterator1 = RandomIterFactory.create(original, null);

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                iterator1.getPixel(i, j, pixelImagemOriginal);

                if (algoritmo == 0) {
                    vetorFeatures = FeatureAssembly.montaFeaturePerPixelTeste(pixelImagemOriginal);
                    //vetorFeatures = Utils.normalizaVetorFeatures(vetorFeatures);

                    redeNeural.setInput(vetorFeatures);
                    redeNeural.calculate();

                    //System.out.println(redeNeural.getOutput()[0]);
                    if (redeNeural.getOutput()[0] >= 0.9 && redeNeural.getOutput()[0] <= 1.1) { //pixel de não pele
                        pixelImagemOriginal[0] = 255;
                        pixelImagemOriginal[1] = 255;
                        pixelImagemOriginal[2] = 255;
                    } else { //pixel de pele
                        pixelImagemOriginal[0] = 0;
                        pixelImagemOriginal[1] = 0;
                        pixelImagemOriginal[2] = 0;
                    }
                    outputRaster.setPixel(i, j, pixelImagemOriginal);
                } else if (algoritmo == 1) {
                    pixelResultante = SkinDetection.aplicaPeerKovacSolina(pixelImagemOriginal);
                    outputRaster.setPixel(i, j, pixelImagemOriginal);
                } else if (algoritmo == 2) {
                    pixelResultante = SkinDetection.aplicaSanchezSucarGomez(pixelImagemOriginal);
                    outputRaster.setPixel(i, j, pixelImagemOriginal);
                } else if (algoritmo == 3) {
                    pixelResultante = SkinDetection.aplicaKuhlESilva(pixelImagemOriginal);
                    outputRaster.setPixel(i, j, pixelImagemOriginal);
                } else if (algoritmo == 4) {
                    pixelResultante = SkinDetection.aplicaBuhyianAmpor(pixelImagemOriginal);
                    outputRaster.setPixel(i, j, pixelImagemOriginal);
                }
            }
        }
        TiledImage ti = new TiledImage(imagemReconhecida, imagemReconhecida.getWidth(), imagemReconhecida.getHeight());
        ti.setData(outputRaster);
        return ti.getAsBufferedImage();
    }
}
