package com.mycompany.cronometro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Vector;

public class tela extends WindowAdapter implements ActionListener{
    private Vector vVoltas;
    private Frame janela;
    private Panel painelTimer,painelBotoes;
    private Label lcronometro, lvoltas, ltotal;
    private Button bstart, bpoint, bfim, brelatorio;
    
    private Cronometro c;
    
    public tela() {
        vVoltas =new Vector();
        janela = new Frame();
        janela.setTitle("Cronometro");
        janela.setSize(370,414);
        janela.setBackground(new Color(160,160,160));
        janela.setLayout(null);
        janela.addWindowListener(this);
        
        painelTimer = new Panel();
        painelTimer.setBackground(new Color(128,128,128));
        painelTimer.setSize(350,234);
        painelTimer.setLocation(10,80);
        painelTimer.setLayout(null);
        
        painelBotoes = new Panel();
        painelBotoes.setBackground(new Color(64,128,128));
        painelBotoes.setSize(350,34);
        painelBotoes.setLocation(10,344);
        painelBotoes.setLayout(new FlowLayout());
        
        lcronometro = new Label("00:00:000");
        lcronometro.setFont(new Font("Arial", Font.BOLD, 24));
        lcronometro.setBounds(100, 80, 200, 40);
        painelTimer.add(lcronometro);
        
        c = new Cronometro();
        c.start();       
        
        Timer timerSwing = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lcronometro.setText(c.getTimer());
            }
        });
        timerSwing.start();
        
        bstart = new Button("START");
        bstart.addActionListener(this);
        bpoint = new Button("POINT");
        bpoint.addActionListener(this);
        bfim = new Button("FIM");
        bfim.addActionListener(this);
        brelatorio = new Button("RELATÓRIO");
        brelatorio.addActionListener(this);
        
        painelBotoes.add(bstart);
        painelBotoes.add(bpoint);
        painelBotoes.add(bfim);
        painelBotoes.add(brelatorio);

        
        janela.add(painelTimer);
        janela.add(painelBotoes);
        janela.setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    
    public static void main(String[] args) {
        tela t = new tela();
    }
}
