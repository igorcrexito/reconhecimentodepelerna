/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Igor
 */
public class Utils {

    public static int maxValue(int a, int b, int c) {
        int max = 0;
        if (a >= b && a >= c) {
            max = a;
        } else if (b >= a && b >= c) {
            max = b;
        } else if (c >= a && c >= b) {
            max = c;
        }
        return max;
    }

    public static int minValue(int a, int b, int c) {
        int min = 0;
        if (a <= b && a <= c) {
            min = a;
        } else if (b <= a && b <= c) {
            min = b;
        } else if (c <= a && c <= b) {
            min = c;
        }
        return min;
    }

    public static float[] calculateHSVValues(int red, int green, int blue) {
        float[] canaisHSV = new float[3];

        double A = (double)(1 / 2) * (double)((red - green) + (red - blue));
        double B = (double)Math.sqrt(Math.pow((red - green), 2) + (red - green) * (green - blue));

        if (B==0 || A==0)
            canaisHSV[0] = 0;
        else    
            canaisHSV[0] = (float) Math.acos(A / B);
        
        if ((red + green + blue) != 0) {
            canaisHSV[1] = 1 - 3 * ((minValue(red, blue, red)) / (red + green + blue));
        } else {
            canaisHSV[1] = 1;
        }
        canaisHSV[2] = (1 / 3) * (red + green + blue);

        return canaisHSV;
    }

    public static float[] calculateYUVValues(int red, int green, int blue) {
        float[] canaisYUV = new float[3];

        canaisYUV[0] = (float) (0.299 * red + 0.587 * green + 0.114 * blue);
        canaisYUV[1] = (float) (-0.147 * red + -0.289 * green + 0.436 * blue);
        canaisYUV[2] = (float) (0.615 * red + -0.515 * green + -0.100 * blue);


        return canaisYUV;
    }

    public static float[] calculateYIQValues(int red, int green, int blue) {
        float[] canaisYIQ = new float[3];

        canaisYIQ[0] = (float) (0.299 * red + 0.587 * green + 0.114 * blue);
        canaisYIQ[1] = (float) (0.596 * red + -0.275 * green + -0.321 * blue);
        canaisYIQ[2] = (float) (0.212 * red + -0.528 * green + 0.321 * blue);


        return canaisYIQ;
    }

    public static float calculateWrValues(int red, int green, int blue) {
        if (red + green + blue != 0) {
            double termoA = (red / (red + green + blue)) - (1 / 3);
            double termoB = (2 / (red + green + blue)) - (1 / 3);

            float Wr = (float) (Math.pow(termoA, 2) + Math.pow(termoB, 2));
            return Wr;
        } else {
            return 0;
        }
    }

    public static void salvaArquivoTreinamento(ArrayList<float[]> featureList) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleRNA/features.txt");
            for (int i=0;i<featureList.size();i++) {
                float[] features = featureList.get(i);
                for (int j=0;j<10;j++) {
                    writer.write(String.valueOf(features[j])+ " ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static PlanarImage reconheceImagem(PlanarImage original, NeuralNetwork redeNeural) {
        PlanarImage imagemReconhecida = original;
        
        int[] pixelImagemOriginal = new int[3];
        
        double[] vetorFeatures = new double[9];
        
        Raster inputRaster = imagemReconhecida.getData();

        WritableRaster outputRaster = inputRaster.createCompatibleWritableRaster();
        RandomIter iterator1 = RandomIterFactory.create(original, null);
        
       for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                iterator1.getPixel(i, j, pixelImagemOriginal); 
                 
                vetorFeatures = FeatureAssembly.montaFeaturePerPixelTeste(pixelImagemOriginal);
                //vetorFeatures = Utils.normalizaVetorFeatures(vetorFeatures);
                
                redeNeural.setInput(vetorFeatures);
                redeNeural.calculate();
                
                //System.out.println(redeNeural.getOutput()[0]);
                if (redeNeural.getOutput()[0] >=0.9 && redeNeural.getOutput()[0]<=1.05) { //pixel de não pele
                    pixelImagemOriginal[0] = 255; pixelImagemOriginal[1] = 255; pixelImagemOriginal[2] = 255;
                } else { //pixel de pele
                    pixelImagemOriginal[0] = 0; pixelImagemOriginal[1] = 0; pixelImagemOriginal[2] = 0;
                }
                outputRaster.setPixel(i, j, pixelImagemOriginal);
            }
        }
        TiledImage ti = new TiledImage(imagemReconhecida, imagemReconhecida.getWidth(), imagemReconhecida.getHeight());
        ti.setData(outputRaster);
        return ti;
        
    }

    private static double[] normalizaVetorFeatures(double[] vetorFeatures) {
        
        double max = 0;
        double min = 99999;
        
        for (int i=0;i<vetorFeatures.length;i++) {
            if (vetorFeatures[i]>max)
                max = vetorFeatures[i];
            else if (vetorFeatures[i]<min)
                min = vetorFeatures[i];
        }
        
        for (int i=0;i<vetorFeatures.length;i++) {
            vetorFeatures[i] = (vetorFeatures[i] - min) / (max - min); 
        }
        
        return vetorFeatures;
    }
    
    
}
