
package momentumbackpropagation;

import java.io.IOException;
import java.util.Scanner;

public class MomentumBackpropagation {

    public static void main(String[] args) throws IOException {
      
        Scanner in = new Scanner(System.in);
        int araKatmanNoron;
        double momentum,ok,error;
        int epoch,sec;
        YSA ysa=null;
        do{
            System.out.println("1.agı egit");
            System.out.println("2.test et");
            System.out.println("3.cıkıs");
            
            sec=in.nextInt();
            switch (sec){
                case 1:
                    System.out.println("ara katman noron");
                    araKatmanNoron=in.nextInt();
                    System.out.println("momentum");
                    momentum=in.nextDouble();
                    System.out.println("OGRENME KATSAYISI");
                    ok=in.nextDouble();
                    System.out.println("error");
                    error=in.nextDouble();
                    System.out.println("epoch");
                    epoch=in.nextInt();
                    ysa=new YSA(araKatmanNoron, momentum, ok, error, epoch);
                    ysa.Egit();
                    System.out.println("egitimdeki hata degeri"+ysa.egitimHata());
                    System.in.read();
                    break;
                case 2:
                    if(ysa==null){
                        System.out.println("once egitim");
                        System.in.read();
                        break;
                    }
                    else{
                        double[] inputs =new double[8];
                        System.out.println("silindir:");
                        inputs[0]=in.nextDouble();
                        System.out.println("beygir gucu:");
                        inputs[1]=in.nextDouble();
                        System.out.println("agirlik");
                        inputs[2]=in.nextDouble();
                        System.out.println("hızlanma");
                        inputs[3]=in.nextDouble();
                        System.out.println("model");
                        inputs[4]=in.nextDouble();
                        System.out.println("ulke");
                        String ulke=in.next();
                        switch(ulke){
                            case "amerika":
                                inputs[6]=0;
                                inputs[7]=0;
                                break;
                            case "asya":
                                inputs[6]=0;
                                inputs[7]=1;
                                break;
                            case "avrupa":
                                inputs[6]=1;
                                inputs[7]=0;
                                break;
                        }
                      System.out.println("yakit:"+ysa.tekTest(inputs));
                      System.in.read();
                        
                    }
                    break;
            }
            
        }while(sec!=3);
    }
    
}
