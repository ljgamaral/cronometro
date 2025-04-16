package com.mycompany.cronometro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

public class Cronometro extends Thread {

    private boolean rodando = true;
    private int miliseg = 0;
    private int minutos = 0;
    private int seg = 0;
    private int milisRestantes = 0;
    private String timerFormatado;
    private boolean iniciado = false;

    public synchronized void startar() 
        {if (!iniciado) {
            this.start();
            iniciado = true;
        }
        rodando = true;
        this.notify(); // Notifica a thread para sair do wait()
    }

    public void run() {
        while (true) {
            synchronized (this) {
                while (!rodando) {
                    try {
                        this.wait(); // Espera até que seja notificada
                    } catch (InterruptedException e) {
                        return; // Encerra se a thread for interrompida
                    }
                }
            }
         
            minutos = (miliseg / 60000) % 60; // Cálculo correto dos minutos
            seg = (miliseg / 1000) % 60;      // Cálculo correto dos segundos
            milisRestantes = miliseg % 1000; // Milissegundos restantes

            timerFormatado = String.format("%02d:%02d:%03d", minutos, seg, milisRestantes);
            try {
                Thread.sleep(1); // Espera 100 milissegundos
            } catch (InterruptedException e) {
                rodando = false; // Interrompe o cronômetro se ocorrer uma exceção
            }

            miliseg += 1; // Aumenta o tempo em 100 milissegundos
        }
    }

    public void parar() {
        rodando = false;
    }
    
    public void zerar() {
        miliseg = 0;
        minutos = 0;
        seg = 0;
        milisRestantes = 0;
        timerFormatado = "00:00:000";
    }
    
    public String getTimer() {
        return timerFormatado;
    }
}
