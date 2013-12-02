/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skinsegmentation;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import utils.Utils;

/**
 *
 * @author Igor
 */
public class SkinDetection {

    public static int[] aplicaPeerKovacSolina(int[] pixel) {

        if (pixel[0] > 95 && pixel[1] > 40 && pixel[2] > 20) {
            if (Utils.maxValue(pixel[0], pixel[1], pixel[2]) - Utils.minValue(pixel[0], pixel[1], pixel[2]) > 15) {
                if (pixel[0] > pixel[1] && pixel[0] > pixel[2]) {
                    pixel[0] = 255;
                    pixel[1] = 255;
                    pixel[2] = 255;
                } else {
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                }
            } else {
                pixel[0] = 0;
                pixel[1] = 0;
                pixel[2] = 0;
            }
        } else {
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
        }

        return pixel;
    }

    public static int[] aplicaSanchezSucarGomez(int[] pixel) {

        float[] hsvValues = new float[3];
        float[] yuvValues = new float[3];
        double Wr = 0;

        hsvValues = Utils.calculateHSVValues(pixel[0], pixel[1], pixel[2]);
        yuvValues = Utils.calculateYUVValues(pixel[0], pixel[1], pixel[2]);
        Wr = Utils.calculateWrValues(pixel[0], pixel[1], pixel[2]);

        if (hsvValues[0] > -17.4545 && hsvValues[0] < 26.6666) {
            if (pixel[2] - yuvValues[0] < -5.9216) {
                if (Wr < 0.0271) {
                    pixel[0] = 255;
                    pixel[1] = 255;
                    pixel[2] = 255;
                } else {
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                }
            } else {
                pixel[0] = 0;
                pixel[1] = 0;
                pixel[2] = 0;
            }
        } else {
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
        }

        return pixel;
    }

    public static int[] aplicaKuhlESilva(int[] pixel) {

        double redNorm = (double) pixel[0] / (double) (pixel[0] + pixel[1] + pixel[2]);
        double greenNorm = (double) pixel[1] / (double) (pixel[0] + pixel[1] + pixel[2]);
        double blueNorm = (double) pixel[2] / (double) (pixel[0] + pixel[1] + pixel[2]);
        double value = (redNorm + greenNorm + blueNorm) / 3;

        if (redNorm >= 0.35 && redNorm <= 0.465) {
            if (greenNorm >= 0.28 && greenNorm <= 0.363) {
                if (value >= 0.25 && value <= 1) {
                    pixel[0] = 255;
                    pixel[1] = 255;
                    pixel[2] = 255;
                } else {
                    pixel[0] = 0;
                    pixel[1] = 0;
                    pixel[2] = 0;
                }
            } else {
                pixel[0] = 0;
                pixel[1] = 0;
                pixel[2] = 0;
            }
        } else {
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
        }


        return pixel;
    }

    public static int[] aplicaBuhyianAmpor(int[] pixel) {

        float[] yiqValues = new float[3];
        yiqValues = Utils.calculateYIQValues(pixel[0], pixel[1], pixel[2]);

        if (yiqValues[0] >= 60 && yiqValues[0] <= 200) {
            if (yiqValues[1] > 20 && yiqValues[1] < 50) {
                pixel[0] = 255;
                pixel[1] = 255;
                pixel[2] = 255;
            } else {
                pixel[0] = 0;
                pixel[1] = 0;
                pixel[2] = 0;
            }
        } else {
            pixel[0] = 0;
            pixel[1] = 0;
            pixel[2] = 0;
        }

        return pixel;
    }
}
