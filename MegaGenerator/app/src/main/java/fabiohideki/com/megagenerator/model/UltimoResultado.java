package fabiohideki.com.megagenerator.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by hidek on 03/02/2018.
 */
@Parcel
public class UltimoResultado {

    @SerializedName("concurso")
    int nroConcurso;

    @SerializedName("resultadoOrdenado")
    String resultado;

    @SerializedName("data")
    String dataConcurso;

    @SerializedName("acumulado")
    int acumulou;

    @SerializedName("vr_estimativa")
    double estPremioProxConc;

    @SerializedName("dt_proximo_concurso")
    String dataProxConc;

    @SerializedName("ganhadores")
    int ganhadoresSena;

    @SerializedName("valor")
    double valorSena;

    @SerializedName("ganhadores_quina")
    int ganhadoresQuina;

    @SerializedName("valor_quina")
    double valorQuina;

    @SerializedName("ganhadores_quadra")
    int ganhadoresQuadra;

    @SerializedName("valor_quadra")
    double valorQuadra;


    public UltimoResultado() {

    }

    public UltimoResultado(int nroConcurso, String resultado, String dataConcurso, int acumulou, double estPremioProxConc, String dataProxConc, int ganhadoresSena, double valorSena, int ganhadoresQuina, double valorQuina, int ganhadoresQuadra, double valorQuadra) {
        this.nroConcurso = nroConcurso;
        this.resultado = resultado;
        this.dataConcurso = dataConcurso;
        this.acumulou = acumulou;
        this.estPremioProxConc = estPremioProxConc;
        this.dataProxConc = dataProxConc;
        this.ganhadoresSena = ganhadoresSena;
        this.valorSena = valorSena;
        this.ganhadoresQuina = ganhadoresQuina;
        this.valorQuina = valorQuina;
        this.ganhadoresQuadra = ganhadoresQuadra;
        this.valorQuadra = valorQuadra;
    }

    public int getNroConcurso() {
        return nroConcurso;
    }

    public void setNroConcurso(int nroConcurso) {
        this.nroConcurso = nroConcurso;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDataConcurso() {

        return dataConcurso;
    }

    public void setDataConcurso(String dataConcurso) {
        this.dataConcurso = dataConcurso;
    }

    public int isAcumulou() {
        return acumulou;
    }

    public void setAcumulou(int acumulou) {
        this.acumulou = acumulou;
    }

    public double getEstPremioProxConc() {
        return estPremioProxConc;
    }

    public void setEstPremioProxConc(double estPremioProxConc) {
        this.estPremioProxConc = estPremioProxConc;
    }

    public String getDataProxConc() {

        return dataProxConc;
    }

    public void setDataProxConc(String dataProxConc) {
        this.dataProxConc = dataProxConc;
    }

    public int getGanhadoresSena() {
        return ganhadoresSena;
    }

    public void setGanhadoresSena(int ganhadoresSena) {
        this.ganhadoresSena = ganhadoresSena;
    }

    public double getValorSena() {
        return valorSena;
    }

    public void setValorSena(double valorSena) {
        this.valorSena = valorSena;
    }

    public int getGanhadoresQuina() {
        return ganhadoresQuina;
    }

    public void setGanhadoresQuina(int ganhadoresQuina) {
        this.ganhadoresQuina = ganhadoresQuina;
    }

    public double getValorQuina() {
        return valorQuina;
    }

    public void setValorQuina(double valorQuina) {
        this.valorQuina = valorQuina;
    }

    public int getGanhadoresQuadra() {
        return ganhadoresQuadra;
    }

    public void setGanhadoresQuadra(int ganhadoresQuadra) {
        this.ganhadoresQuadra = ganhadoresQuadra;
    }

    public double getValorQuadra() {
        return valorQuadra;
    }

    public void setValorQuadra(double valorQuadra) {
        this.valorQuadra = valorQuadra;
    }
}
