
package   momentumbackpropagation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;


public class YSA {
    private static final File egitimDosya=new File(YSA.class.getResource("Egitim.txt").getPath());
    private static final File testDosya=new File(YSA.class.getResource("Test.txt").getPath());
    
    private double [] maksimumlar;
    private double [] minimumlar;
    private DataSet egitimVeriSeti;
    private DataSet testVeriSeti;
   
    
    private int araKatmanNoron;
    MomentumBackpropagation bp;
    
    public YSA(int araKatmanNoron,double momentum,double ok,double error,int epoch) throws FileNotFoundException{
     maksimumlar=new double[8];
     minimumlar=new double[8];
     for(int i=0;i<8;i++){
       maksimumlar[i]=Double.MAX_VALUE;
       minimumlar[i]=Double.MIN_VALUE;
     }
    EgitimVeriSetiMaks();
    TestVeriSetiMaks();
    
    egitimVeriSeti=EgitimVeriSeti();
    
    testVeriSeti=TestVeriSeti();
    bp=new MomentumBackpropagation();
    bp.setMomentum(momentum);
    bp.setLearningRate(ok);
    bp.setMaxError(error);
    bp.setMaxIterations(epoch);
    this.araKatmanNoron=araKatmanNoron;
     
    }

    public void Egit(){
        MultiLayerPerceptron sinirselAg=new MultiLayerPerceptron(TransferFunctionType.SIGMOID,8,araKatmanNoron,3);
        sinirselAg.setLearningRule(bp);
        sinirselAg.learn(egitimVeriSeti);
        sinirselAg.save("a.nnet");
        System.out.println("Egitim Tamamlandi");
        
    }
    
    private double MSE(double [] beklenen,double [] cikti){
        double satirHata=0;
        for(int i=0;i<3;i++){
            satirHata+=Math.pow(beklenen[i]-cikti[i],2);
        }
        return satirHata/3;
    }
    
    public double Test(){
        //nnet dosyasında agırlıklar kaydedılır
        NeuralNetwork sinirselAg=NeuralNetwork.createFromFile("a.nnet");
        
        //hata hesaplama yöntemi MSE olacak.0 ile 1 arasında cikacak sonuclar icin kullanilir
        
        double toplamHata=0;
        for(DataSetRow r: testVeriSeti){
         sinirselAg.setInput(r.getInput());
         sinirselAg.calculate();
         toplamHata+=MSE(r.getDesiredOutput(),sinirselAg.getOutput());
         
         }
        
        return toplamHata/testVeriSeti.size();
    }
    
    public String sonuc(double [] outputs){
       int indeks = 0;
        double maks = outputs[0];
        if(outputs[1] > maks)
        {
            maks = outputs[1];
            indeks = 1;
        }
        if(outputs[2] > maks)
        {
            maks = outputs[2];
            indeks = 2;
        }
        
        if(indeks == 0) return "Kötü";
        if(indeks == 1) return "Normal";
        if(indeks == 2) return "İyi";
        return "";

         
    }
    
    public String tekTest(double[] inputs){
      for(int i=0;i<8;i++){
       inputs[i]=min_max(maksimumlar[i], minimumlar[i],inputs[i]);       
      }
      
     NeuralNetwork sinirselAg=NeuralNetwork.createFromFile("a.nnet");
     sinirselAg.setInput(inputs);
     sinirselAg.calculate();
     return sonuc(sinirselAg.getOutput());
    }
    
    public double egitimHata(){
     return bp.getTotalNetworkError();
    }
    
    public DataSet getegitimVeriSeti(){
      return egitimVeriSeti;
    }
    
    public DataSet gettestVeriSeti(){
      return testVeriSeti;
    }
    
    private void EgitimVeriSetiMaks() throws FileNotFoundException {
        Scanner oku = new Scanner(egitimDosya);
        while (oku.hasNextDouble()) {
            for(int i=0;i<8;i++){
              double d=oku.nextDouble();
              if(d>maksimumlar[i])maksimumlar[i]=d;
              if(d<minimumlar[i])minimumlar[i]=d;
            }
            //output verileri geçilmesi için
           oku.nextDouble();oku.nextDouble();oku.nextDouble();
        }
        oku.close();
    }

    private void TestVeriSetiMaks() throws FileNotFoundException {
        Scanner oku = new Scanner(testDosya);
        while (oku.hasNextDouble()) {
            for(int i=0;i<8;i++){
              double d=oku.nextDouble();
              if(d>maksimumlar[i])maksimumlar[i]=d;
              if(d<minimumlar[i])minimumlar[i]=d;
            }
           oku.nextDouble();oku.nextDouble();oku.nextDouble();
        }
        oku.close();
    }
  
    private double min_max(double max,double min,double x){
        return (x-min)/(max-min);
    }
            
    private DataSet EgitimVeriSeti() throws FileNotFoundException {
      Scanner oku = new Scanner(egitimDosya);      
      DataSet egitim=new DataSet(8,3);
          
      while(oku.hasNextDouble()){
       double [] inputs=new double[8];
        
        for(int i=0;i<8;i++){
            double d = oku.nextDouble();           
            inputs[i]=min_max(maksimumlar[i], minimumlar[i], d);
        }
          //ilk parametre inputlar, ikinci parametre output değerleri bu degerlere mın maks donusumu yapılmaz
          DataSetRow satir=new DataSetRow(inputs,new double[]{oku.nextDouble(),oku.nextDouble(),oku.nextDouble()});
          egitim.add(satir);
          
      } 
      oku.close();
      return egitim;
    }
    
    private DataSet TestVeriSeti() throws FileNotFoundException {
    Scanner oku = new Scanner(testDosya);
    //8 input 3 output
      DataSet egitim=new DataSet(8,3);
      while(oku.hasNextDouble()){
       double [] inputs=new double[8];
        for(int i=0;i<8;i++){
            double d = oku.nextDouble();
            inputs[i]=min_max(maksimumlar[i], minimumlar[i], d);
        }
          DataSetRow satir=new DataSetRow(inputs,new double[]{oku.nextDouble(),oku.nextDouble(),oku.nextDouble()});
          egitim.add(satir);
      } 
      oku.close();
      return egitim;
    }  
    
    
}
