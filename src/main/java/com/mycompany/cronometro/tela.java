package com.mycompany.cronometro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Vector;

public class tela extends WindowAdapter implements ActionListener{
    private Frame janela;
    private Panel painelTimer,painelBotoes;
    private Label lcronometro, ltotal;
    private Button bstart, bparar, bpoint, bfim, brelatorio;
    private TextArea tvoltas;
    private Vector vVoltas;
        
    private Cronometro c;
    private Timer timerSwing;
    
    public tela() {
        c = new Cronometro();
        vVoltas = new Vector();
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
        lcronometro.setBounds(100, 10, 200, 40);
        tvoltas = new TextArea("", 30, 30, TextArea.SCROLLBARS_NONE);
        tvoltas.setBounds(50, 50, 200, 150);
        tvoltas.setEditable(false);
        painelTimer.add(lcronometro);
        painelTimer.add(tvoltas);
                        
        bstart = new Button("START");
        bstart.addActionListener(this);
        bparar = new Button("PARAR");
        bparar.addActionListener(this);
        bpoint = new Button("POINT");
        bpoint.addActionListener(this);
        bfim = new Button("FIM");
        bfim.addActionListener(this);
        brelatorio = new Button("RELATÓRIO");
        brelatorio.addActionListener(this);
        
        painelBotoes.add(bstart);
        painelBotoes.add(bparar);
        painelBotoes.add(bpoint);
        painelBotoes.add(bfim);
        painelBotoes.add(brelatorio);

        
        janela.add(painelTimer);
        janela.add(painelBotoes);
        janela.setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent e) {
        Button b=(Button)e.getSource();
        if (b==bstart) {
            this.botaoStart();
        } else if (b==bparar) {
            this.botaoParar();
        } else if (b==bpoint) {
            this.botaoPoint();
        } else if (b==bfim) {
            this.botaoFim();
        }
    }
    
    void voltasFormatadas(Vector vVoltas) {
        String formatoAntes = "";
        for (int i = 0; i < vVoltas.size(); i++) {
            formatoAntes += (i+1) + " - " + vVoltas.get(i) + "\n";
        }
        tvoltas.setText(formatoAntes);
    }
    
    void botaoStart () {
        c.startar();
        timerSwing = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lcronometro.setText(c.getTimer());
            }
        });
        timerSwing.start();
    }
    
    void botaoParar() {
        c.parar();
        timerSwing.stop();
    }
    
    void botaoPoint() {
        vVoltas.addElement(c.getTimer());
        voltasFormatadas(vVoltas);
    }
    
    void botaoFim() {
        c.parar();
        c.zerar();
        lcronometro.setText(c.getTimer());
        timerSwing.stop();
    }
    
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    
    public static void main(String[] args) {
        tela t = new tela();
    }
}
