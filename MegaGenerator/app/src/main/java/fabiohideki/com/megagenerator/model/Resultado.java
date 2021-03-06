package fabiohideki.com.megagenerator.model;

import com.google.gson.annotations.SerializedName;

import java.util.TreeSet;

/**
 * Created by fabio.lagoa on 05/02/2018.
 */

public class Resultado {

    @SerializedName("concurso")
    private int numero;

    @SerializedName("data")
    private String data;

    @SerializedName("resultadoOrdenado")
    private String dezenas;

    private TreeSet<Integer> dezenasSet;

    public Resultado() {
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDezenas() {
        return dezenas;
    }

    public void setDezenas(String dezenas) {
        this.dezenas = dezenas;
    }

    public TreeSet<Integer> getDezenasSet() {

        String[] dezenas = this.dezenas.split("-");

        TreeSet<Integer> set = new TreeSet<>();

        for (int i = 0; i < dezenas.length; i++) {
            set.add(Integer.parseInt(dezenas[i]));
        }

        return set;
    }
}
