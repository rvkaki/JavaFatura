
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
    
    public String lerNIF(String pedido){
        String nif;
        do{
            nif = ler(pedido);
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2' && nif.charAt(0) != '5' && !nif.equals("000000000")));
        //posteriormente remover nif.equals("000000000")

        return nif;
    }

    public static void main(String[] args){
        Plataforma plataforma = new Plataforma();
        // No futuro, ler de ficheiros o conteúdo das faturas/entidades e fazer setFaturas/setEntidades

        boolean exit = false;
        while (!exit) {
            exit = plataforma.printMenu();
        }
    }

    public String ler(String pedido){
        System.out.println("Escreva " + pedido);
        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        return res;
    }

    public void pausaParaLer(){
        System.out.println("Pressione Enter para continuar...");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        s.close();
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
    
    public boolean printMenu(){
        StringBuilder menu = new StringBuilder();
        int escolha;

        menu.append("               ##############################################              \n");
        menu.append("               #                JavaFatura                  #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #               1 --> Login                  #              \n");
        menu.append("               #               2 --> Registar               #              \n");
        menu.append("               #               3 --> Sair                   #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3);
        s.close();

        if (escolha == 1) {
            boolean sucesso = this.login();
            if (sucesso) {
                if (this.utilizador instanceof Admin)
                    printMenuAdmin();
                else if (this.utilizador instanceof Individual)
                    printMenuIndividual();
                else if (this.utilizador instanceof Coletivo)
                    printMenuColetivo();
            }
        } else if(escolha == 2) {
            this.registar();
            this.login();
        } else if (escolha == 3)
            return true;

        return false;
    }

    public void registar(){ 
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

        nif = lerNIF("NIF");

        Entidade e = null;
        if (this.totalEntidades.containsKey(nif)) {
            e = this.totalEntidades.get(nif);
            if (!e.getPassword().equals("")) {
                System.out.println("NIF já registado!");
                pausaParaLer();
                return;
            }
        } else {
            if (nif.charAt(0) == '1' || nif.charAt(0) == '2')
                e = new Individual();
            else if (nif.charAt(0) == '5')
                e = new Coletivo();
            else if (nif.equals("000000000"))
                e = new Admin();
            
            e.setNIF(nif);
        }
        
        String nome = ler("nome");
        e.setNome(nome);
        String email = ler("email");
        e.setEmail(email);
        String morada = ler("morada");
        e.setMorada(morada);
        String password = ler("password");
        e.setPassword(password);

        if(nif.charAt(0) == '1' || nif.charAt(0) == '2')
            registarIndividual((Individual) e);
        
        else if(nif.charAt(0) == '5')
            registarColetivo((Coletivo) e);

        this.totalEntidades.put(nif, e.clone());
        this.utilizador = e;
    }

    public void registarIndividual(Individual e){
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
            String nifFamiliar = lerNIF("NIF " + (i+1));
            nifAgregado.add(nifFamiliar);
        }
        ArrayList<String> codigosAtividades = new ArrayList<String>();
        for(String s: this.atividades){
            if(lerAtividade(s))
                codigosAtividades.add(s);
        }
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
        
        e.setNumeroAgregadoFamiliar(numeroAgregado);
        e.setNIFAgregado(nifAgregado);
        e.setCoeficienteFiscal(coeficiente);
        e.setCodigosAtividades(codigosAtividades);
    }

    public void registarColetivo(Coletivo e){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarColetivo                #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #       Designação:                          #              \n");
        menu.append("               #       Coeficiente Fiscal:                  #              \n");
        menu.append("               #       Atividades Económicas:               #              \n");
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

        e.setDesignacao(designacao);
        e.setInformacaoAtividades(informacaoAtividades);
        e.setCoeficienteFiscal(coeficiente);
    }

    public void logout(){
        this.utilizador = null;
    }

    public boolean login(){
        String nif;
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
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2' && nif.charAt(0) != '5' && !nif.equals("000000000")));

        if (this.totalEntidades.containsKey(nif) && this.totalEntidades.get(nif).getPassword().equals("")) {
            Entidade e = this.totalEntidades.get(nif);
            String password = e.getPassword();
            String tentativa;
            do{
                tentativa = ler("Password");
            }while(! password.equals(tentativa));

            this.utilizador = e;
        } else {
            System.out.println("NIF não existente!");
            System.out.println("Confirme se o NIF que introduziu está correto e se estiver registe-se");
            pausaParaLer();
            return false;
        }

        return true;
    }

    public void printMenuIndividual(){
        int escolha;
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #           Contribuinte Individual          #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           1 --> Ver Faturas                #              \n");
        menu.append("               #           2 --> Validar Faturas            #              \n");
        menu.append("               #           3 --> Valor Dedução Fiscal       #              \n");
        menu.append("               #           4 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4);
        s.close();

        if(escolha == 1)
            verFaturas(this.utilizador);
        else if (escolha == 2)
            // Fazer
            ;
        else if (escolha == 3)
            // Fazer
            ;
        else if (escolha == 4)
            logout();
    }

    public void printMenuColetivo(){
        int escolha;
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #           Contribuinte Coletivo            #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           1 --> Emitir Fatura              #              \n");
        menu.append("               #           2 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        

        if(escolha == 1){
            String nifCliente = lerNIF("NIF do cliente");
            String descricao = ler("descricao da fatura");
            double valor;
            do{
                System.out.println("Escreva valor da Fatura");
                valor = s.nextDouble();
            }while(valor < 0);
            s.close();
            emitirFatura(nifCliente, descricao, valor);
        } else if (escolha == 2)
            logout();
    }

    public void printMenuAdmin(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Admin                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
    }

    public void verFaturas(Entidade e){
        ArrayList<Integer> listaFaturas = e.getListaFaturas();
        for(Integer i: listaFaturas){
            System.out.println(this.totalFaturas.get(i).toString());
        }
    }

    public void emitirFatura(String nifCliente, String descricao, double valor) {
        String nifEmitente = this.utilizador.getNIF();
        Entidade e = this.totalEntidades.get(nifEmitente);

        // Entidades individuais não podem emitir faturas
        if (e instanceof Individual)
            return;

        Coletivo empresa = (Coletivo) e;
        String atividade = "";
        if (empresa.getInformacaoAtividades().size() == 1)
            atividade = empresa.getInformacaoAtividades().get(0);

        Fatura f = new Fatura(nifEmitente, LocalDate.now(), nifCliente, descricao, atividade, valor);
        this.totalFaturas.add(f.clone());
        int indiceFatura = this.totalFaturas.indexOf(f);

        empresa.adicionarFatura(indiceFatura);
        if (!this.totalEntidades.containsKey(nifCliente)) {
            Individual i = new Individual();
            i.setNIF(nifCliente);
            this.totalEntidades.put(nifCliente, i.clone());
        }
        this.totalEntidades.get(nifCliente).adicionarFatura(indiceFatura);
    }
}
