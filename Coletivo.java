
/**
 * Subclasse que representa a entidade do coletivo
 *
 * @author GC-JRI-RV
 * @version 24/04/2018
 */ 

import java.util.ArrayList;

public class Coletivo extends Entidade{
    /** Designação do coletivo */
    private String designacao;
    /** Lista com informações das atividades económicas */
    private ArrayList<String> informacaoAtividades;
    /** Número do coeficiente fiscal */
    private double coeficienteFiscal;
    /**
     * Construtor por omissão
     */
    public Coletivo(){
        super();
        this.designacao = "";
        this.informacaoAtividades = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
    }
    /**
     * Construtor por parametro
     * @param nif
     * @param email
     * @param nome
     * @param morada
     * @param password
     * @param listafaturas
     * @param designacao
     * @param informacaoAtividade
     * @param coeficienteFiscal
     */
    public Coletivo(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, String designacao, ArrayList<String> informacaoAtividades, double coeficienteFiscal){
        super(nif, email, nome, morada, password, listaFaturas);
        this.designacao = designacao;
        this.informacaoAtividades = new ArrayList<String>(informacaoAtividades.size());
        for(String s: informacaoAtividades)
            this.informacaoAtividades.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
    }
    /**
     * Construtor por cópia
     * @param e Coletivo original
     */
    public Coletivo(Coletivo e){
        super(e);
        this.designacao = e.getDesignacao();
        this.informacaoAtividades = e.getInformacaoAtividades();
        this.coeficienteFiscal = e.getCoeficienteFiscal();
    }
    /** 
     * Devolve a designacao do coletivo
     * @return a designacao
     */
    public String getDesignacao(){
        return this.designacao;
    }
    /**
     * Devolve a informação das atividades do coletivo
     * @return a informacao das atividades
     */
    public ArrayList<String> getInformacaoAtividades(){
        ArrayList<String> res = new ArrayList<String>(this.informacaoAtividades.size());
        for(String s: this.informacaoAtividades)
            res.add(s);
        return res;
    }
    /**
     * Devolve o coeficiente fiscal
     * @return o coeficiente fiscal
     */
    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }
    /** 
     * Define a designação do coletivo
     * @param designacao
     */
    public void setDesignacao(String designacao){
        this.designacao = designacao;
    }
    /**
     * Define as informações de atividade
     * @param informacaoAtividades
     */
    public void setInformacaoAtividades(ArrayList<String> informacaoAtividades){
        ArrayList<String> res = new ArrayList<String>(informacaoAtividades.size());
        for(String s: informacaoAtividades)
            res.add(s);
        this.informacaoAtividades = res;
    }
    /**
     * Define o coeficiente fiscal
     * @param coeficiente
     */
    public void setCoeficienteFiscal(double coeficiente){
        this.coeficienteFiscal = coeficiente;
    }
    /**
     * Devolve a ????????
     * @return 
     */
    public String getAtividadeSeUnica() {
        if (this.informacaoAtividades.size() == 1)
            return this.informacaoAtividades.get(0);
        else
            return "";
    }
    /**
     * Cria uma cópia do objeto
     * @return cópia do objeto 
    */
    public Coletivo clone(){
        return new Coletivo(this);
    }
}
