/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neural;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.InstarLearning;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Igor
 */
public class RedeNeural {

    int tamanhoVetorFeature = 10;
    double[][] entradas;
    private NeuralNetwork neuralNetwork;
    int numeroTotalPixels = 0;

    public RedeNeural(int rede) {
        // load saved neural network
        neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, tamanhoVetorFeature-1, 5, 1);
        neuralNetwork = NeuralNetwork.load("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleRNA/rede.nnet");
    }

    public RedeNeural() {
        numeroTotalPixels = contaTamanhoArquivo();
        entradas = new double[numeroTotalPixels][tamanhoVetorFeature];
        neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, tamanhoVetorFeature-1, 35, 1);

        final DataSet trainingSet = DataSet.createFromFile("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleRNA/features.txt", 9, 1, " ");
        trainingSet.normalize();
        
        neuralNetwork.learn(trainingSet);
        
        //save trained neural network
//        neuralNetwork.save("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleNN/rede.nnet");

        

    }

    private int leArquivo() {
        int count = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleRNA/features.txt"));
            String str;
            while (in.ready()) {
                str = in.readLine();
                montaMatriz(str, count);
                count++;
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("não achou o arquivo");
        }

        return count;
    }

    private void montaMatriz(String linha, int c) {
        String[] partes = new String[tamanhoVetorFeature];
        double num;

        partes = linha.split(" ");
        for (int j = 0; j < tamanhoVetorFeature; j++) {
            num = Double.parseDouble(partes[j]);
            entradas[c][j] = num;
        }
    }

    private double[] normalizaDados(double[] dados) {
        for (int i = 0; i < tamanhoVetorFeature; i++) {
            if (dados[i] <= 50) {
                dados[i] = 0;
            } else {
                dados[i] = 1;
            }
        }

        return dados;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    private int contaTamanhoArquivo() {
        int count = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader("C:/Users/Igor/Documents/NetBeansProjects/ReconhecimentoDePeleRNA/features.txt"));
            String str;
            while (in.ready()) {
                str = in.readLine();
                count++;
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("não achou o arquivo");
        }

        return count;
    }

    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
                        
            System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }
}
