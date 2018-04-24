
/**
 * Write a description of class Individual here.
 *
 * @author RV
 * @version 15/04/2018
 */

import java.util.ArrayList;

public class Individual extends Entidade{
    
    private int numeroAgregadoFamiliar;
    private ArrayList<String> nifAgregado;
    private double coeficienteFiscal;
    private ArrayList<String> codigosAtividades;


    public Individual(){
        super();
        this.numeroAgregadoFamiliar = 0;
        this.nifAgregado = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
        this.codigosAtividades = new ArrayList<String>();
    }

    public Individual(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, int numeroAgregadoFamiliar, ArrayList<String> nifAgregado, double coeficienteFiscal, ArrayList<String> codigosAtividades){
        super(nif, email, nome, morada, password, listaFaturas);
        this.numeroAgregadoFamiliar = numeroAgregadoFamiliar;
        this.nifAgregado = new ArrayList<String>();
        for(String s: nifAgregado)
            this.nifAgregado.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
        this.codigosAtividades = new ArrayList<String>();
        for(String s: codigosAtividades)
            this.codigosAtividades.add(s);
    }

    public Individual(Individual c){
        super(c);
        this.numeroAgregadoFamiliar = c.getNumeroAgregadoFamiliar();
        this.nifAgregado = c.getNIFAgregadoFamiliar();
        this.coeficienteFiscal = c.getCoeficienteFiscal();
        this.codigosAtividades = c.getCodigosAtividades();
    }

    public int getNumeroAgregadoFamiliar(){
        return this.numeroAgregadoFamiliar;
    }

    public ArrayList<String> getNIFAgregadoFamiliar(){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: this.nifAgregado)
            res.add(s);
        return res;
    }

    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }

    public ArrayList<String> getCodigosAtividades(){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: this.codigosAtividades)
            res.add(s);
        return res;
    }

    public void setNumeroAgregadoFamiliar(int numero){
        this.numeroAgregadoFamiliar = numero;
    }

    public void setNIFAgregado(ArrayList<String> nifs){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: nifs)
            res.add(s);
        this.nifAgregado = res;
    }

    public void setCoeficienteFiscal(double coeficiente){
        this.coeficienteFiscal = coeficiente;
    }

    public void setCodigosAtividades(ArrayList<String> codigos){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: codigos)
            res.add(s);
        this.codigosAtividades = res;
    }

    public Individual clone(){
        return new Individual(this);
    }
}
