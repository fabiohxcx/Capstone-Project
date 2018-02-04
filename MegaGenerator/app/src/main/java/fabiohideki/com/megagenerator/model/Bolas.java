package fabiohideki.com.megagenerator.model;

import org.parceler.Parcel;

import java.util.Arrays;

/**
 * Created by hidek on 04/02/2018.
 */
@Parcel
public class Bolas {

    int bola1;
    int bola2;
    int bola3;
    int bola4;
    int bola5;
    int bola6;

    public Bolas() {

    }

    public Bolas(int bola1, int bola2, int bola3, int bola4, int bola5, int bola6) {

        this.bola1 = bolaValida(bola1) ? bola1 : 0;
        this.bola2 = bolaValida(bola2) ? bola2 : 0;
        this.bola3 = bolaValida(bola3) ? bola3 : 0;
        this.bola4 = bolaValida(bola4) ? bola4 : 0;
        this.bola5 = bolaValida(bola5) ? bola5 : 0;
        this.bola6 = bolaValida(bola6) ? bola6 : 0;

    }

    public String getOrderedBallsString() {

        int[] bolasInteger = new int[]{bola1, bola2, bola3, bola4, bola5, bola6};

        Arrays.sort(bolasInteger);

        return bolasInteger[0] + "-" + bolasInteger[1] + "-" + bolasInteger[2] + "-" + bolasInteger[3] + "-" + bolasInteger[4] + "-" + bolasInteger[5];
    }

    boolean bolaValida(int numero) {

        return numero > 0 && numero < 61;

    }

    public int getBola1() {
        return bola1;
    }

    public int getBola2() {
        return bola2;
    }

    public int getBola3() {
        return bola3;
    }

    public int getBola4() {
        return bola4;
    }

    public int getBola5() {
        return bola5;
    }

    public int getBola6() {
        return bola6;
    }

    //Get in String

    public String getBola1String() {
        return Integer.toString(bola1);
    }

    public String getBola2String() {
        return Integer.toString(bola2);
    }

    public String getBola3String() {
        return Integer.toString(bola3);
    }

    public String getBola4String() {
        return Integer.toString(bola4);
    }

    public String getBola5String() {
        return Integer.toString(bola5);
    }

    public String getBola6String() {
        return Integer.toString(bola6);
    }

}
