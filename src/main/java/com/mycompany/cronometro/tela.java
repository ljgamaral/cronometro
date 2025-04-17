package com.mycompany.cronometro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Vector;
import java.sql.*;

public class tela extends WindowAdapter implements ActionListener{
    private Frame janela;
    private Panel painelTimer,painelBotoes;
    private Label lcronometro, ltotal;
    private Button bstart, bparar, bpoint, bzerar, brelatorio, bsalvar;
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
        bzerar = new Button("ZERAR");
        bzerar.addActionListener(this);
        bsalvar = new Button("SALVAR");
        bsalvar.addActionListener(this);
        brelatorio = new Button("RELATÓRIO");
        brelatorio.addActionListener(this);
        
        painelBotoes.add(bstart);
        painelBotoes.add(bparar);
        painelBotoes.add(bpoint);
        painelBotoes.add(bzerar);
        painelBotoes.add(bsalvar);
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
        } else if (b==bzerar) {
            this.botaoFim();
        } else if (b==bsalvar) {
            this.botaoSalvar();
        }
    }
    
    void voltasFormatadas(Vector vVoltas) {
        String formatoAntes = "";
        for (int i = 0; i < vVoltas.size(); i++) {
            formatoAntes += (i+1) + " - " + vVoltas.get(i) + "\n";
        }
        tvoltas.setText(formatoAntes);
    }
    public Connection conecta() {
        String url = "jdbc:mysql://localhost:3306/cronometro?useTimezone=true&serverTimezone=UTC";
        Connection con;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // driver atualizado
            con = DriverManager.getConnection(url, "root", ""); // ajuste a senha se necessário
            return con;
        } catch (ClassNotFoundException cnf) {
            System.out.println("Erro no DRIVER: " + cnf.getMessage());
            return null;
        } catch (SQLException sql) {
            System.out.println("Erro no SQL: " + sql.getMessage());
            return null;
        }
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
    void botaoSalvar(){
        Connection con = conecta();
        try {
            int resultado;
            Statement st = con.createStatement();
            for (int i= 0; i < vVoltas.size(); i++) {
                resultado = st.executeUpdate("insert into voltas (cod_volta, tempo)values("+(i + 1)+"," + vVoltas.get(i) + ");");
            }
            st. close();
            con.close();
        } catch (SQLException sql){
            System.out.println("Não salvo");
        }
    }
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    
    public static void main(String[] args) {
        tela t = new tela();
    }
}

