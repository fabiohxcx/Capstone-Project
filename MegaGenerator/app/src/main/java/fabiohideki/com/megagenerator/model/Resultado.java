package fabiohideki.com.megagenerator.model;

/**
 * Created by fabio.lagoa on 05/02/2018.
 */

public class Resultado {

    private int numero;
    private String data;
    private String dezenas;

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
}
