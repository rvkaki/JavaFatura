
/**
 * Write a description of class Interface here.
 * @author (your name)
 * @version (a version number or a date)
 */

import java.lang.System;
import java.util.Scanner;

public class Interface{
    
    public void printMenu(){
        StringBuilder menu = new Stringbuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                JavaFatura                  #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #             Opção 1 --> Login              #              \n");
        menu.append("               #             Opção 2 --> Registar           #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.println(menu);

        Scanner s = new Scanner(in);
        do{
            int escolha = s.nextInt();
        }while(escolha != 1 || escolha != 2);
        s.close();

        if(escolha == 1)
            this.printMenuLogin();
        
        if(escolha == 2)
            this.printMenuRegistar();

    }

    public void printMenuLogin(){
        StringBuilder menu = new Stringbuilder();

        menu.append("               ##############################################              \n");
        menu.append("               #                  Login                     #              \n");
        menu.append("               ##############################################              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #              Introduza o NIF:              #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               #                                            #              \n");
        menu.append("               ##############################################              \n");
        System.out.println(menu);
    }


}
