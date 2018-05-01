
/**
 * Write a description of class Coletivo here.
 *
 * @author RV
 * @version 24/04/2018
 */ 

import java.util.ArrayList;

public class Coletivo extends Entidade{
    private String designacao;
    private ArrayList<String> informacaoAtividades;
    private double coeficienteFiscal;

    public Coletivo(){
        super();
        this.designacao = "";
        this.informacaoAtividades = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
    }

    public Coletivo(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, String designacao, ArrayList<String> informacaoAtividades, double coeficienteFiscal){
        super(nif, email, nome, morada, password, listaFaturas);
        this.designacao = designacao;
        this.informacaoAtividades = new ArrayList<String>();
        for(String s: informacaoAtividades)
            this.informacaoAtividades.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
    }

    public Coletivo(Coletivo e){
        super(e);
        this.designacao = e.getDesignacao();
        this.informacaoAtividades = e.getInformacaoAtividades();
        this.coeficienteFiscal = e.getCoeficienteFiscal();
    }

    public String getDesignacao(){
        return this.designacao;
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

    public void setDesignacao(String designacao){
        this.designacao = designacao;
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

    public String getAtividadeSeUnica() {
        if (this.informacaoAtividades.size() == 1)
            return this.informacaoAtividades.get(0);
        else
            return "";
    }

    public Coletivo clone(){
        return new Coletivo(this);
    }
}
