/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.image.Raster;
import java.util.ArrayList;
import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;

/**
 *
 * @author Igor
 */
public class FeatureAssembly {

    public static float[] montaFeaturePerPixel(int[] pixelImagemOriginal, int[] pixelMascaraBinaria) {
        float[] vetorDeFeaturePorPixel = new float[10];

        //primeira feature => canais RGB do pixel
        vetorDeFeaturePorPixel[0] = pixelImagemOriginal[0];
        vetorDeFeaturePorPixel[1] = pixelImagemOriginal[1];
        vetorDeFeaturePorPixel[2] = pixelImagemOriginal[2];

        //segunda feature => canais HSV do pixel
         float[] vetorHSV = Utils.calculateHSVValues(pixelImagemOriginal[0], pixelImagemOriginal[1], pixelImagemOriginal[2]);
         vetorDeFeaturePorPixel[3] = vetorHSV[0];
         vetorDeFeaturePorPixel[4] = vetorHSV[1];
         vetorDeFeaturePorPixel[5] = vetorHSV[2];

        //terceira feature => canais YUV ou YCbCr
        float[] vetorYUV = Utils.calculateYUVValues(pixelImagemOriginal[0], pixelImagemOriginal[1], pixelImagemOriginal[2]);
        vetorDeFeaturePorPixel[6] = vetorYUV[0];
        vetorDeFeaturePorPixel[7] = vetorYUV[1];
        vetorDeFeaturePorPixel[8] = vetorYUV[2];

        //última posição do vetor => 1 para pele e 0 para não pele
        
        if (pixelMascaraBinaria[0] == 1) {
            vetorDeFeaturePorPixel[9] = 1;
        } else {
            vetorDeFeaturePorPixel[9] = 0;
        }
        return vetorDeFeaturePorPixel;
    }

    public static double[] montaFeaturePerPixelTeste(int[] pixelImagemOriginal) {
        double[] vetorDeFeaturePorPixel = new double[9];

        //primeira feature => canais RGB do pixel
        vetorDeFeaturePorPixel[0] = pixelImagemOriginal[0];
        vetorDeFeaturePorPixel[1] = pixelImagemOriginal[1];
        vetorDeFeaturePorPixel[2] = pixelImagemOriginal[2];

        
        //segunda feature => canais HSV do pixel
         float[] vetorHSV = Utils.calculateHSVValues(pixelImagemOriginal[0], pixelImagemOriginal[1], pixelImagemOriginal[2]);
         vetorDeFeaturePorPixel[3] = vetorHSV[0];
         vetorDeFeaturePorPixel[4] = vetorHSV[1];
         vetorDeFeaturePorPixel[5] = vetorHSV[2];

        //terceira feature => canais YUV ou YCbCr
        float[] vetorYUV = Utils.calculateYUVValues(pixelImagemOriginal[0], pixelImagemOriginal[1], pixelImagemOriginal[2]);
        vetorDeFeaturePorPixel[6] = vetorYUV[0];
        vetorDeFeaturePorPixel[7] = vetorYUV[1];
        vetorDeFeaturePorPixel[8] = vetorYUV[2];
        
        return vetorDeFeaturePorPixel;
    }

    
    public static ArrayList<float[]> montaFeaturePerImage(PlanarImage imagemOriginal, PlanarImage imagemMascaraBinaria) {

        ArrayList<float[]> featuresPerPixel = new ArrayList<float[]>();
        
        int[] pixelImagemOriginal = new int[3];
        int[] pixelImagemMascaraBinaria = new int[3];

        RandomIter iterator1 = RandomIterFactory.create(imagemOriginal, null);
        RandomIter iterator2 = RandomIterFactory.create(imagemMascaraBinaria, null);

        for (int i = 0; i < imagemOriginal.getWidth(); i++) {
            for (int j = 0; j < imagemOriginal.getHeight(); j++) {
                iterator1.getPixel(i, j, pixelImagemOriginal);
                iterator2.getPixel(i, j, pixelImagemMascaraBinaria);
                
                featuresPerPixel.add(montaFeaturePerPixel(pixelImagemOriginal, pixelImagemMascaraBinaria));               
            }
        }
        return featuresPerPixel;
    }
}
