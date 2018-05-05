
/**
 * Subclasse que representa a entidade individual
 *
 * @author GC-JRI-RV
 * @version 15/04/2018
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Individual extends Entidade{
    /** Número do agregado familiar*/
    private int numeroAgregadoFamiliar;
    /** Lista com os nif do agregado */
    private ArrayList<String> nifAgregado;
    /** Numero do coeficiente fiscal */
    private double coeficienteFiscal;
    /** Map com atividades e valor deduzido para cada */
    private HashMap<String, Double> codigosAtividades;

    /**
     * Construtor por omissão
     */
    public Individual(){
        super();
        this.numeroAgregadoFamiliar = 0;
        this.nifAgregado = new ArrayList<String>();
        this.coeficienteFiscal = 0.0;
        this.codigosAtividades = new HashMap<String,Double>();
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
    public Individual(String nif, String email, String nome, String morada, String password, ArrayList<Integer> listaFaturas, int numeroAgregadoFamiliar, ArrayList<String> nifAgregado, double coeficienteFiscal, HashMap<String,Double> codigosAtividades){
        super(nif, email, nome, morada, password, listaFaturas);
        this.numeroAgregadoFamiliar = numeroAgregadoFamiliar;
        this.nifAgregado = new ArrayList<String>();
        for(String s: nifAgregado)
            this.nifAgregado.add(s);
        this.coeficienteFiscal = coeficienteFiscal;
        this.codigosAtividades = new HashMap<String,Double>();
        for(String s: codigosAtividades.keySet())
            this.codigosAtividades.put(s, codigosAtividades.get(s));
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
    public HashMap<String,Double> getCodigosAtividades(){
        HashMap<String,Double> res = new HashMap<String,Double>();
        for(String s: this.codigosAtividades.keySet())
            res.put(s, this.codigosAtividades.get(s));
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
    public void setCodigosAtividades(HashMap<String,Double> codigos){
        HashMap<String,Double> res = new HashMap<String,Double>();
        for(String s: codigos.keySet())
            res.put(s, codigos.get(s));
        this.codigosAtividades = res;
    }

    /**
     * Atualiza a entrada de uma determinada atividade no codigosAtividades
     * @param key chave 
     * @param value valor 
     */
    public void atualizaCodigosAtividades(String key, double valor){
        if(this.codigosAtividades.containsKey(key)){
            this.codigosAtividades.replace(key, valor);
        }else{
            this.codigosAtividades.put(key, valor);
        }
    }
    /** 
     * Cria uma cópia do objecto
     * @return cópia do objeto
     */
    public Individual clone(){
        return new Individual(this);
    }
}