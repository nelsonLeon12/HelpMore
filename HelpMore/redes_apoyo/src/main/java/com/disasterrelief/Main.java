package com.disasterrelief;

import com.disasterrelief.service.AdminService;
import com.disasterrelief.service.UserService;


public class Main {
    public static void main(String[] args) {

        System.out.println("SISTEMA DE REDES DE APOYO EN DESASTRES ");
        System.out.println("Universidad Popular del Cesar - 2025");


        // Llama sus métodos con try-catch por excepción específica
        UserService userService = new UserService();
        AdminService adminService = new AdminService();


    }
}
