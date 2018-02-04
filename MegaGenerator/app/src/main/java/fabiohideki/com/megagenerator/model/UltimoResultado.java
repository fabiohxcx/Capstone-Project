package fabiohideki.com.megagenerator.model;

import org.parceler.Parcel;

/**
 * Created by hidek on 03/02/2018.
 */
@Parcel
public class UltimoResultado {

    int nroConcurso;
    Bolas bolas;
    String dataConcurso;
    boolean acumulou;
    String estPremioProxConc;
    String dataProxConc;
    String sena;
    String quina;
    String quadra;

    public UltimoResultado() {

    }

    public UltimoResultado(int nroConcurso, Bolas bolas, String dataConcurso, boolean acumulou, String estPremioProxConc, String dataProxConc, String sena, String quina, String quadra) {
        this.nroConcurso = nroConcurso;
        this.bolas = bolas;
        this.dataConcurso = dataConcurso;
        this.acumulou = acumulou;
        this.estPremioProxConc = estPremioProxConc;
        this.dataProxConc = dataProxConc;
        this.sena = sena;
        this.quina = quina;
        this.quadra = quadra;
    }

    public int getNroConcurso() {
        return nroConcurso;
    }

    public void setNroConcurso(int nroConcurso) {
        this.nroConcurso = nroConcurso;
    }

    public Bolas getBolas() {
        return bolas;
    }

    public void setBolas(Bolas bolas) {
        this.bolas = bolas;
    }

    public String getDataConcurso() {
        return dataConcurso;
    }

    public void setDataConcurso(String dataConcurso) {
        this.dataConcurso = dataConcurso;
    }

    public boolean isAcumulou() {
        return acumulou;
    }

    public void setAcumulou(boolean acumulou) {
        this.acumulou = acumulou;
    }

    public String getEstPremioProxConc() {
        return estPremioProxConc;
    }

    public void setEstPremioProxConc(String estPremioProxConc) {
        this.estPremioProxConc = estPremioProxConc;
    }

    public String getDataProxConc() {
        return dataProxConc;
    }

    public void setDataProxConc(String dataProxConc) {
        this.dataProxConc = dataProxConc;
    }

    public String getSena() {
        return sena;
    }

    public void setSena(String sena) {
        this.sena = sena;
    }

    public String getQuina() {
        return quina;
    }

    public void setQuina(String quina) {
        this.quina = quina;
    }

    public String getQuadra() {
        return quadra;
    }

    public void setQuadra(String quadra) {
        this.quadra = quadra;
    }
}
