
/**
 * Write a description of class Plataforma here.
 *
 * @author RV
 * @version 16/04/2018
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.lang.System;
import java.time.LocalDate;
import java.util.Arrays;

public class Plataforma{
    private ArrayList<Fatura> totalFaturas; 
    private HashMap<String,Entidade> totalEntidades;
    private Entidade utilizador;
    private static ArrayList<String> atividades = new ArrayList<>(Arrays.asList("despesas gerais familiares",
                                                                   "saude", "educaçao", "habitaçao", "lares",
                                                                   "reparaçao de automoveis", "reparacao de motociclos", 
                                                                   "restauraçao e alojamento", "cabeleireiros",
                                                                   "atividades veterinarias", "transportes"));

    public Plataforma(){
        this.totalFaturas = new ArrayList<Fatura>();
        this.totalEntidades = new HashMap<String,Entidade>();
        this.utilizador = null;
    }
    
    public static void main(String[] args){
        Plataforma plataforma = new Plataforma();
        // No futuro, ler de ficheiros o conteúdo das faturas/entidades e fazer setFaturas/setEntidades
        plataforma.printMenu();
    }

    public String ler(String pedido){
        System.out.println("Escreva " + pedido);
        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        return res;
    }

    public boolean lerAtividade(String pedido){
        System.out.println("Desconta para " + pedido + "? (s/n)");
        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        if(res.equals("s"))
            return true;
        return false;
    }
    
    public void printMenu(){
        StringBuilder menu = new StringBuilder();
        int escolha;

        menu.append("               ##############################################              \n");
        menu.append("               #                JavaFatura                  #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             Opção 1 --> Login              #              \n");
        menu.append("               #             Opção 2 --> Registar           #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 || escolha != 2);
        s.close();

        if(escolha == 1)
            this.logIn();
        
        if(escolha == 2)
            this.registar();

    }

    public boolean registar(){ 
        String nif;
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                 Registar                   #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             NIF:                           #              \n");
        menu.append("               #             Nome:                          #              \n");
        menu.append("               #             Email:                         #              \n");
        menu.append("               #             Morada:                        #              \n");
        menu.append("               #             Password:                      #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        do{
            nif = ler("NIF");
        }while(nif.length() != 9 || nif.charAt(0) != '1' || nif.charAt(0) != '2' || nif.charAt(0) != '5');

        if(this.totalEntidades.containsKey(nif)){
            Entidade e = this.totalEntidades.get(nif);
            if(e.getPassword().equals(""))
                this.utilizador = e;
            else{
                System.out.println("NIF já registado");
                return false;
            }
        }
        
        String nome = ler("nome");
        String email = ler("email");
        String morada = ler("morada");
        String password = ler("password");

        if(nif.charAt(0) == '1' || nif.charAt(0) == '2')
            registarIndividual(nif, email, nome, morada, password, this.utilizador);
        
        else if(nif.charAt(0) == '5')
            registarColetivo(nif, email, nome, morada, password, this.utilizador);
        
        return true;
    }

    public void registarIndividual(String nif, String email, String nome, String morada, String password, Entidade utilizador){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarIndividual              #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #       Número de elementos agregado:        #              \n");
        menu.append("               #       NIF dos elementos agregado:          #              \n");
        menu.append("               #       Atividades Económica:                #              \n");
        menu.append("               #       Coeficiente Fiscal:                  #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        int numeroAgregado = Integer.parseInt(ler("numero de elementos do Agregado Familiar"));
        ArrayList<String> nifAgregado = new ArrayList<String>(numeroAgregado);
        for(int i=0; i<numeroAgregado; i++){
            String nifFamiliar = ler("nif " + i);
            nifAgregado.add(nifFamiliar);
        }
        ArrayList<String> codigosAtividades = new ArrayList<String>();
        for(String s: this.atividades){
            if(lerAtividade(s))
                codigosAtividades.add(s);
        }
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
        if(utilizador == null){
            utilizador = new Individual(nif, email, nome, morada, password, 
                                        new ArrayList<Integer>(), numeroAgregado, 
                                                nifAgregado, coeficiente, codigosAtividades);
            this.totalEntidades.put(nif, utilizador);
        }else{
            Individual util = (Individual) utilizador;
            util.setEmail(email);
            util.setNome(nome);
            util.setMorada(morada);
            util.setPassword(password);
            util.setNumeroAgregadoFamiliar(numeroAgregado);
            util.setNIFAgregado(nifAgregado);
            util.setCoeficienteFiscal(coeficiente);
            util.setCodigosAtividades(codigosAtividades);
        }

    }

    public void registarColetivo(String nif, String email, String nome, String morada, String password, Entidade utilizador){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarColetivo                #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #       Designação:                          #              \n");
        menu.append("               #       Coeficiente Fiscal:                  #              \n");
        menu.append("               #       Atividades Económica:                #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        String designacao = ler("designacao");
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
        ArrayList<String> informacaoAtividades = new ArrayList<String>();
        for(String s: this.atividades){
            if(lerAtividade(s))
                informacaoAtividades.add(s);
        }
        if(utilizador == null){
            utilizador = new Coletivo(nif, email, nome, morada, password, 
                                      new ArrayList<Integer>(), designacao,
                                      informacaoAtividades, coeficiente);
            this.totalEntidades.put(nif, utilizador);
        }else{
            Coletivo util = (Coletivo) utilizador;
            util.setEmail(email);
            util.setNome(nome);
            util.setMorada(morada);
            util.setPassword(password);
            util.setInformacaoAtividades(informacaoAtividades);
            util.setCoeficienteFiscal(coeficiente);
        }
    }

    public void logOut(){
        this.utilizador = null;
    }

    public void logIn(){
        String nif;
        String tentativa;
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Login                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             NIF:                           #              \n");
        menu.append("               #             Password:                      #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        do{
            nif = ler("NIF");
        }while(nif.length() != 9 || nif.charAt(0) != '1' || nif.charAt(0) != '2' || nif.charAt(0) != '5' || nif.charAt(0) != '0');

        
        if(! this.totalEntidades.containsKey(nif)){
            System.out.println("NIF não existente!\nRegiste-se primeiro");
            this.registar();
        }
        else{
            Entidade e = this.totalEntidades.get(nif);
            String password = e.getPassword();
            do{
                tentativa = ler("Password");
            }while(! password.equals(tentativa));

            this.utilizador = e;
        }
    }

    public void emitirFatura(String nifEmitente, String nifCliente, String descricao, double valor) {
        Entidade e = this.totalEntidades.get(nifEmitente);
        if (!(e instanceof Coletivo))
            return;

        Coletivo empresa = (Coletivo) e;
        String atividade = "";
        if (empresa.getInformacaoAtividades().size() == 1)
            atividade = empresa.getInformacaoAtividades().get(0);

        Fatura f = new Fatura(nifEmitente, LocalDate.now(), nifCliente, descricao, atividade, valor);
        this.totalFaturas.add(f.clone());
    }
}
