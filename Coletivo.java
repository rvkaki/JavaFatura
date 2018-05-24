
/**
 * Subclasse que representa a entidade do coletivo
 *
 * @author GC-JRI-RV
 * @version 24/04/2018
 */ 

import java.util.ArrayList;
import java.util.Arrays;

public class Coletivo extends Entidade{
    /** Designação do coletivo */
    private String designacao;
    /** Lista com informações das atividades económicas */
    private ArrayList<String> informacaoAtividades;
    /** Número do coeficiente fiscal */
    private double coeficienteFiscal;
    /** Distritos de interior */
    private static ArrayList<String> distritosInterior = new ArrayList<>(Arrays.asList("vila real","bragança","viseu","guarda","castelo branco", "portalegre", "évora","evora", "santarém","santarem", "beja"));
    /** Distrito da Empresa */
    private String distrito;

    /**
     * Construtor por omissão
     */
    public Coletivo(){
        super();
        this.designacao = "";
        this.informacaoAtividades = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
        this.distrito = "";
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
     * @param distrito
     */
    public Coletivo(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, String designacao, ArrayList<String> informacaoAtividades, double coeficienteFiscal, String distrito){
        super(nif, email, nome, morada, password, listaFaturas);
        this.designacao = designacao;
        this.informacaoAtividades = new ArrayList<String>(informacaoAtividades.size());
        for(String s: informacaoAtividades)
            this.informacaoAtividades.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
        this.distrito = distrito;
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
        this.distrito = e.getDistrito();
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
     * Devolve o número de atividades do coletivo
     * @return número de atividades
     */
    public int getNumeroAtividades(){
        return this.informacaoAtividades.size();
    }

    /**
     * Devolve o coeficiente fiscal
     * @return o coeficiente fiscal
     */
    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }

    /**
     * Devolve o distrito da empresa
     * @return o distrito
     */
    public String getDistrito(){
        return this.distrito;
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
     * Define se a empresa é do interior
     * @param interior
     */
    public void setDistrito(String distrito){
        this.distrito = distrito;
    }

    /**
     * Devolve a atividade da empresa se só tiver uma associada ou uma String vazia, caso contrário
     * @return a atividade se for única, String vazia caso contrário
     */
    public String getAtividadeSeUnica() {
        if (this.informacaoAtividades.size() == 1)
            return this.informacaoAtividades.get(0);
        else
            return "";
    }
    /** Devolve true se a Empresa for do interior
     * @return true se for do interior, false caso contrário
     */
    public boolean isInterior(){
        return(distritosInterior.contains(this.distrito.toLowerCase()));
    }
    /**
     * Cria uma cópia do objeto
     * @return cópia do objeto 
    */
    public Coletivo clone(){
        return new Coletivo(this);
    }
}
