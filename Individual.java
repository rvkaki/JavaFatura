
/**
 * Subclasse que representa a entidade individual
 *
 * @author RV
 * @version 15/04/2018
 */

import java.util.ArrayList;

public class Individual extends Entidade{
    /** Número do agregado familiar*/
    private int numeroAgregadoFamiliar;
    /** Lista com os nif do agregado */
    private ArrayList<String> nifAgregado;
    /** Numero do coeficiente fiscal */
    private double coeficienteFiscal;
    /** Lista dos codigos das atividades */
    private ArrayList<String> codigosAtividades;

    /**
     * Construtor por omissão
     */
    public Individual(){
        super();
        this.numeroAgregadoFamiliar = 0;
        this.nifAgregado = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
        this.codigosAtividades = new ArrayList<String>();
    }
    /**
     * Construtor por parametro
     * @param nif
     * @param email
     * @param nome
     * @param morada
     * @param password
     * @param listaFaturas
     * @param numeroAgregadoFamiliar
     * @param nifAgregado
     * @param coeficienteFiscal
     * @param codigosAtividades
     */
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
    /**
     * Contrutor por cópia 
     * @param c Individual orignal
     */
    public Individual(Individual c){
        super(c);
        this.numeroAgregadoFamiliar = c.getNumeroAgregadoFamiliar();
        this.nifAgregado = c.getNIFAgregadoFamiliar();
        this.coeficienteFiscal = c.getCoeficienteFiscal();
        this.codigosAtividades = c.getCodigosAtividades();
    }
    /**
     * Devolve o numero do agregado familiar
     * @return numero do agregado familiar
     */
    public int getNumeroAgregadoFamiliar(){
        return this.numeroAgregadoFamiliar;
    }
    /**
     * Devolve o nif do agregado familiar
     * @return nif
     */
    public ArrayList<String> getNIFAgregadoFamiliar(){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: this.nifAgregado)
            res.add(s);
        return res;
    }
    /**
     * Devolve o coeficiente fiscal
     * @return coeficiente fiscal
     */
    public double getCoeficienteFiscal(){
        return this.coeficienteFiscal;
    }
    /**
     * Devolve os codigos das atividades
     * @return codigos das atividades
     */
    public ArrayList<String> getCodigosAtividades(){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: this.codigosAtividades)
            res.add(s);
        return res;
    }
    /**
     * Define o numero do agregado familiar
     * @param numero
     */
    public void setNumeroAgregadoFamiliar(int numero){
        this.numeroAgregadoFamiliar = numero;
    }
    /** 
     * Define o nif dos agragados
     * @param nifs
     */
    public void setNIFAgregado(ArrayList<String> nifs){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: nifs)
            res.add(s);
        this.nifAgregado = res;
    }
    /**
     * Define o coeficiente fiscal 
     * @param coeficiente
     */
    public void setCoeficienteFiscal(double coeficiente){
        this.coeficienteFiscal = coeficiente;
    }
    /**
     * Define os codigos de atividades
     * @param codigos
     */
    public void setCodigosAtividades(ArrayList<String> codigos){
        ArrayList<String> res = new ArrayList<String>();
        for(String s: codigos)
            res.add(s);
        this.codigosAtividades = res;
    }
    /** 
     * Cria uma cópia do objecto
     * @return cópia do objeto
     */
    public Individual clone(){
        return new Individual(this);
    }
}