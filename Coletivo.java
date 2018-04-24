
/**
 * Write a description of class Coletivo here.
 *
 * @author RV
 * @version 15/04/2018
 */ 

import java.util.ArrayList;

public class Coletivo extends Entidade{
    private ArrayList<String> informacaoAtividades;
    private double coeficienteFiscal;

    public Coletivo(){
        super();
        this.informacaoAtividades = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
    }

    public Coletivo(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas,ArrayList<String> informacaoAtividades, double coeficienteFiscal){
        super(nif, email, nome, morada, password, listaFaturas);
        this.informacaoAtividades = new ArrayList<String>();
        for(String s: informacaoAtividades)
            this.informacaoAtividades.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
    }

    public Coletivo(Coletivo e){
        super(e);
        this.informacaoAtividades = e.getInformacaoAtividades();
        this.coeficienteFiscal = e.getCoeficienteFiscal();
    }

    public ArrayList<String> getInformacaoAtividades(){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: this.informacaoAtividades)
            res.add(s);
        return res;
    }

    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }

    public void setInformacaoAtividades(ArrayList<String> informacaoAtividades){
        ArrayList<String> res = new ArrayList<String>(informacaoAtividades.size());
        for(String s: informacaoAtividades)
            res.add(s);
        this.informacaoAtividades = res;
    }

    public void setCoeficienteFiscal(double coeficiente){
        this.coeficienteFiscal = coeficiente;
    }

    public Coletivo clone(){
        return new Coletivo(this);
    }
}
