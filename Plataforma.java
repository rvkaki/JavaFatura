
/**
 * Classe que representa a interface
 *
 * @author GC-JRI-RV
 * @version 16/04/2018
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.HashMap;
import java.lang.System;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Exception;

public class Plataforma{
    /**Lista de todas as faturas */
    private ArrayList<Fatura> totalFaturas;
    /** Lista de todas as entidades*/ 
    private HashMap<String,Entidade> totalEntidades;
    /**Identificação da entidade */
    private Entidade utilizador;
    /**Lista das atividades económicas */
    private static ArrayList<String> atividades = new ArrayList<>(Arrays.asList("despesas gerais familiares",
                                                                   "saude", "educaçao", "habitaçao", "lares",
                                                                   "reparaçao de automoveis", "reparacao de motociclos", 
                                                                   "restauraçao e alojamento", "cabeleireiros",
                                                                   "atividades veterinarias", "transportes"));
    /**
     * Construtor por omissão
     */                                                              
   public Plataforma(){
        this.totalFaturas = new ArrayList<Fatura>();
        this.totalEntidades = new HashMap<String,Entidade>();
        this.utilizador = null;
    }

    /**
     * Ler o NIF
     * @param pedido
     * @return nif
     */
    public String lerNIF(String pedido){
        String nif;
        do{
            nif = ler(pedido);
        }while(nif.length() != 9 || (nif.charAt(0) != '1' && nif.charAt(0) != '2' && nif.charAt(0) != '5' && !nif.equals("000000000")));
        //posteriormente remover nif.equals("000000000")

        return nif;
    }

    /**
     * Grava os dados para um ficheiro
     */
    public void save(){
        try{
            FileOutputStream novoEstado = new FileOutputStream("estado.sav");
            ObjectOutputStream save = new ObjectOutputStream(novoEstado);
            save.writeObject(this.totalFaturas);
            save.writeObject(this.totalEntidades);
            save.close();
            novoEstado.close();
        }
        catch(Exception exc){
            System.out.println("Não foi possível guardar o estado no ficheiro");
            pausaParaLer();
        }
    }

    /**
     * Carrega os dados de um ficheiro
     * @return plataforma com os dados carregados
     */
    public void load(){
        try{
            FileInputStream estado = new FileInputStream("estado.sav");
            if(estado.available() > 0){
                ObjectInputStream restore = new ObjectInputStream(estado);
                ArrayList<Fatura> totalFaturas = (ArrayList<Fatura>) restore.readObject();
                HashMap<String,Entidade> totalEntidades = (HashMap<String,Entidade>) restore.readObject();
                this.totalFaturas = totalFaturas;
                this.totalEntidades = totalEntidades;
                restore.close();
            }
            estado.close();
        }
        catch(Exception exc){
            System.out.println("Não foi possível ler o estado do ficheiro");
            pausaParaLer();
        }
    }

    /**
     * Dá início ao menu
     * @param????
     */
    public static void main(String[] args){
        Plataforma plataforma = new Plataforma();
        plataforma.load();

        boolean exit = false;
        while (!exit)
            exit = plataforma.printMenu();

        plataforma.save();
    }
    /**
     * Ler pedido
     * @param pedido
     * @return leitura do input
     */
    public String ler(String pedido){
        System.out.println("Escreva " + pedido);
        Scanner ler = new Scanner(System.in);
        String res = ler.nextLine();
        ler.close();
        return res;
    }
    /**
     * ???
     */
    public void pausaParaLer(){
        System.out.println();
        System.out.println("Pressione Enter para continuar...");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        s.close();
    }
    /**
     * Ler atividades económicas do coletivo
     * @return lista com as atividades do coletivo
     */
    public ArrayList<String> lerAtividadesColetivo() {
        ArrayList<String> codigosAtividades = new ArrayList<String>();
        Scanner ler = new Scanner(System.in);
        for (String atividade: this.atividades) {
            System.out.println("Atua na área de " + atividade + "? (s/n)");
            String res = ler.nextLine();
            if (res.equals("s"))
                codigosAtividades.add(atividade);
        }
        ler.close();

        return codigosAtividades;
    }
    /**
     * Imprime o menu inicial
     * @return true se o utilizador quiser sair da plataforma, false caso queira continuar a utilizá-la
     */
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

        if (escolha == 1)
            this.login();
        else if(escolha == 2)
            this.registar();
        else if (escolha == 3)
            return true;

        return false;
    }
    /**
     * Registar no site
     */
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
                e = new Entidade();
            
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

    }
    /**
     * Registar uma entidade individual
     * @param e
     */
    public void registarIndividual(Individual e){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            RegistarIndividual              #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #       Número de elementos agregado:        #              \n");
        menu.append("               #       NIF dos elementos agregado:          #              \n");
        menu.append("               #       Coeficiente Fiscal:                  #              \n");
        menu.append("               #       Rendimento anual agregado:           #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);
        
        int numeroAgregado = Integer.parseInt(ler("numero de elementos do Agregado Familiar"));
        ArrayList<String> nifAgregado = new ArrayList<String>(numeroAgregado);
        if (numeroAgregado > 1) {
            System.out.println("Escreva o NIF dos restantes elementos do agregado familiar");
            for(int i=1; i<numeroAgregado; i++){
                String nifFamiliar = lerNIF("NIF " + i);
                nifAgregado.add(nifFamiliar);
            }
        }
        double coeficiente = Double.parseDouble(ler("coeficiente Fiscal"));
        double rendimentoAtual;
        double rendimentoAgregado;
        String nif = null;

        for(String nif1: nifAgregado)
            if(this.totalEntidades.containsKey(nif1)){
                nif = nif1;
                break;
            }
        
        if(nif != null){
            rendimentoAtual = ((Individual) this.totalEntidades.get(nif)).getRendimentoAgregado();
            Scanner s = new Scanner(System.in);
            String resposta;
            do{
                System.out.println("Confirma que o rendimento anual do seu agregado é " + rendimentoAtual + "? (s/n)");
                resposta = s.nextLine();
            }while(!resposta.equals("s") && !resposta.equals("n"));
            s.close();

            if(resposta.equals("n")){
                rendimentoAgregado = Double.parseDouble(ler("rendimento anual do agregado familiar"));
                for(String n: nifAgregado)
                    ((Individual) this.totalEntidades.get(n)).setRendimentoAgregado(rendimentoAgregado);
            }
            else {
                e.setRendimentoAgregado(rendimentoAtual);
            }
        }
        else {
            rendimentoAgregado = Double.parseDouble(ler("rendimento anual do agregado familiar"));
            e.setRendimentoAgregado(rendimentoAgregado);
        }
        
        e.setNumeroAgregadoFamiliar(numeroAgregado);
        e.setNIFAgregado(nifAgregado);
        e.setCoeficienteFiscal(coeficiente);
    }
    /**
     * Registar uma entidade coletivo
     * @param e
     */
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
        ArrayList<String> informacaoAtividades = lerAtividadesColetivo();

        e.setDesignacao(designacao);
        e.setInformacaoAtividades(informacaoAtividades);
        e.setCoeficienteFiscal(coeficiente);
    }
    /**
     * Fazer o log out
     */
    public void logout(){
        this.save();
        this.utilizador = null;
    }
    /**
     * Fazer o log in
     */
    public void login(){
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

        if (this.totalEntidades.containsKey(nif) && !this.totalEntidades.get(nif).getPassword().equals("")) {
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
            return;
        }

        while (this.utilizador != null) { // Enquanto não fizer logout
            if (nif.equals("000000000"))
                printMenuAdmin();
            else if (this.utilizador instanceof Individual)
                printMenuIndividual();
            else if (this.utilizador instanceof Coletivo)
                printMenuColetivo();
        }
    }
    /**
     * Imprimir o menu de uma entidade individual
     */
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
        menu.append("               #           4 --> Definições da conta        #              \n");
        menu.append("               #           5 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5);
        s.close();

        if(escolha == 1)
            verFaturas();
        else if (escolha == 2)
            validarFaturas();
        else if (escolha == 3)
            // Fazer
            ;
        else if (escolha == 4) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        } else if (escolha == 5)
            logout();
    }

    /**
     * Valida as faturas pendentes
     */
    public void validarFaturas(){
        ArrayList<Fatura> faturasPendentes = new ArrayList<Fatura>();
        for(int i: this.utilizador.getListaFaturas()){
            Fatura f = this.totalFaturas.get(i);
            if(f.estaPendente() && f.getNIFCliente().equals(this.utilizador.getNIF()))
                faturasPendentes.add(f);
        }

        if (faturasPendentes.size() == 0) {
            System.out.println("Não tem faturas para validar. Por favor, volte mais tarde");
            pausaParaLer();

            return;
        }

        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #               Validar Faturas              #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           Tem " + faturasPendentes.size() + " faturas pendente(s)        #              \n");
        menu.append("               #           Deseja validá-las?               #              \n");
        menu.append("               #           1 --> Sim                        #              \n");
        menu.append("               #           2 --> Não, regressar ao menu     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        int escolha;
        Scanner s = new Scanner(System.in);
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2);
        s.close();

        if(escolha == 1){
            for(Fatura f: faturasPendentes){
                System.out.print('\u000C');
                ArrayList<String> atividades = ((Coletivo) this.totalEntidades.get(f.getNIFEmitente())).getInformacaoAtividades();

                System.out.println(f.toString());
                System.out.println("Pode associar esta fatura a:");
                int i = 0;
                for(String a: atividades){
                    System.out.println(i + " --> " + a);
                    i++;
                }

                s = new Scanner(System.in);
                do{
                    escolha = s.nextInt();
                }while(escolha >= atividades.size());
                s.close();

                f.setAtividade(atividades.get(escolha));
                HashMap<String,Double> codigos = ((Individual) this.utilizador).getCodigosAtividades();
                String chave = atividades.get(escolha);
                double valor = codigos.getOrDefault(chave, 0.0) + f.getValor();
                ((Individual) this.utilizador).atualizaCodigosAtividades(chave, valor);

            }
                
        }
        else if(escolha == 2)
            return;
    }

    /**
     * Imprimir o menu de uma entidade coletiva
     */
    public void printMenuColetivo(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #           Contribuinte Coletivo            #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           1 --> Emitir Fatura              #              \n");
        menu.append("               #           2 --> Ver Faturas                #              \n");
        menu.append("               #           3 --> Definições da conta        #              \n");
        menu.append("               #           4 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        int escolha;
        do{
            escolha = s.nextInt();
        }while(escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4);
        

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
        }else if(escolha == 2)
            verFaturas();
        else if (escolha == 3) {
            boolean exit = false;
            while (!exit)
                exit = definicoesDaConta();
        } else if (escolha == 4)
            logout();
    }
    /**
     * Ver ou mudar as definições de conta
     * @return ???
     */
    public boolean definicoesDaConta() {
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #            Definições da Conta             #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #            1 --> Nome                      #              \n");
        menu.append("               #            2 --> Email                     #              \n");
        menu.append("               #            3 --> Morada                    #              \n");
        menu.append("               #            4 --> Password                  #              \n");
        menu.append("               #            5 --> Voltar ao menu            #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            escolha = s.nextInt();
        } while (escolha != 1 && escolha != 2 && escolha != 3 && escolha != 4 && escolha != 5);
        s.close();

        Scanner novasDefinicoes = new Scanner(System.in);
        if (escolha == 1) {
            System.out.println("Introduza o novo nome");
            this.utilizador.setNome(novasDefinicoes.nextLine());
        } else if (escolha == 2) {
            System.out.println("Introduza o novo email");
            this.utilizador.setEmail(novasDefinicoes.nextLine());
        } else if (escolha == 3) {
            System.out.println("Introduza a nova morada");
            this.utilizador.setMorada(novasDefinicoes.nextLine());
        } else if (escolha == 4) {
            String tentativa;
            do {
                System.out.println("Introduza a password atual");
                tentativa = novasDefinicoes.nextLine();
            } while (!this.utilizador.getPassword().equals(tentativa));
            System.out.println("Introduza a nova password");
            this.utilizador.setPassword(novasDefinicoes.nextLine());
        } else if (escolha == 5) {
            novasDefinicoes.close();
            return true;
        }

        novasDefinicoes.close();
        return false;
    }
    /**
     * Imprimir o menu do administrador
     */
    public void printMenuAdmin(){
        StringBuilder menu = new StringBuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Admin                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #           1 --> Logout                     #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.print('\u000C');
        System.out.println(menu);

        Scanner s = new Scanner(System.in);
        int escolha;
        do {
            escolha = s.nextInt();
        } while (escolha != 1);
        s.close();

        if(escolha == 1)
            logout();
    }
    /**
     * Ver as faturas
     */
    public void verFaturas() {
        if (this.utilizador.getListaFaturas().size() == 0)
            System.out.println("Não tem faturas emitidas em seu nome. Por favor, volte mais tarde");

        for (Integer i: this.utilizador.getListaFaturas()) {
            System.out.println(this.totalFaturas.get(i).toString());
        }
        pausaParaLer();
    }
    /**
     * Emitir as faturas
     * @param nifCliente
     * @param descricao
     * @param valor
     */
    public void emitirFatura(String nifCliente, String descricao, double valor) {
        Coletivo empresa = (Coletivo) this.utilizador;
        String atividade = empresa.getAtividadeSeUnica();

        Fatura f = new Fatura(empresa.getNIF(), LocalDateTime.now(), nifCliente, descricao, atividade, valor);
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

    /**
     * Método que ordena as faturas de uma entidade por valor (decrescente)
     * @param nif do cliente ou emissor
     * @return res TreeSet de faturas ordenado decrescentemente
     */
    public TreeSet<Fatura> sortValor(String nif){
        ArrayList<Fatura> faturas = new ArrayList<Fatura>();
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif))
                    faturas.add(this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif))
                    faturas.add(this.totalFaturas.get(i));
            }
        }

        TreeSet<Fatura> res = new TreeSet<Fatura>((f1,f2) -> (int) (f2.getValor() - f1.getValor()));
        for(Fatura f: faturas)
            res.add(f);
        return res;
    }

    /**
     * Método que ordena as faturas de uma entidade por data (decrescente)
     * @param nif do cliente ou emissor
     * @return res TreeSet de faturas ordenado decrescentemente
     */
    public TreeSet<Fatura> sortData(String nif){
        ArrayList<Fatura> faturas = new ArrayList<Fatura>();
        if(this.utilizador instanceof Individual){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFEmitente().equals(nif))
                    faturas.add(this.totalFaturas.get(i));
            }
        }
        else if(this.utilizador instanceof Coletivo){
            for(int i: this.utilizador.getListaFaturas()){
                if(this.totalFaturas.get(i).getNIFCliente().equals(nif))
                    faturas.add(this.totalFaturas.get(i));
            }
        }
        
        TreeSet<Fatura> res = new TreeSet<Fatura>((f1,f2) -> f1.getData().compareTo(f2.getData()));
        for(Fatura f: faturas)
            res.add(f);
        return res;
    }
}
