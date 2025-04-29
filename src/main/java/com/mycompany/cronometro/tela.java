package com.mycompany.cronometro;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JTable;
import java.util.Vector;
import javax.swing.Timer;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class tela extends WindowAdapter implements ActionListener, KeyListener {
    private Frame janela;
    private Panel painelTimer,painelBotoes;
    private Label lcronometro, ltotal;
    private Button bstart, bparar, bzerar, brelatorio, bsalvar;
    private Button[] bpoint;
    private JTable tabela;
    private JScrollPane barraRolagem;
    private TextArea tvoltas;
    private int quantBotoesPoint;
    private Vector<equipe> equipesParticipantes;
    
    private Cronometro c;
    private Timer timerSwing;
    
    private String formatoAntes = "";
    
    public tela(int quantBotoesPoint, Vector<equipe> equipes) {
        c = new Cronometro();
        this.equipesParticipantes = equipes;
        this.quantBotoesPoint = quantBotoesPoint;
        janela = new Frame();
        janela.setTitle("Cronometro");
        janela.setSize(370,414);
        janela.setBackground(new Color(160,160,160));
        janela.setLayout(null);
        janela.addWindowListener(this);
        
        janela.addKeyListener(this);
        janela.addWindowListener(this);
        
        painelTimer = new Panel();
        painelTimer.setBackground(new Color(128,128,128));
        painelTimer.setSize(350,234);
        painelTimer.setLocation(10,80);
        painelTimer.setLayout(null);
        
        painelBotoes = new Panel();
        painelBotoes.setBackground(new Color(64,128,128));
        painelBotoes.setSize(350,80);
        painelBotoes.setLocation(10,344);
        painelBotoes.setLayout(new FlowLayout());
        
        lcronometro = new Label("00:00:000");
        lcronometro.setFont(new Font("Arial", Font.BOLD, 24));
        lcronometro.setBounds(100, 10, 200, 40);
        
        tvoltas = new TextArea("", 30, 30, TextArea.SCROLLBARS_NONE);
        tvoltas.setBounds(25, 50, 300, 150);
        tvoltas.setEditable(false);
        
        painelTimer.add(lcronometro);
        painelTimer.add(tvoltas);
                        
        bstart = new Button("START");
        bstart.addActionListener(this);
        bparar = new Button("PARAR");
        bparar.addActionListener(this);
        bparar.setEnabled(false);
        bpoint = new Button[quantBotoesPoint];
        bzerar = new Button("ZERAR");
        bzerar.addActionListener(this);
        bzerar.setEnabled(false);
        bsalvar = new Button("SALVAR");
        bsalvar.addActionListener(this);
        bsalvar.setEnabled(false);
        brelatorio = new Button("RELATÓRIO");
        brelatorio.addActionListener(this);
        brelatorio.setEnabled(false);
        
        painelBotoes.add(bstart);
        painelBotoes.add(bparar);
        
        for(int i = 0; i < quantBotoesPoint; i++) {
            bpoint[i] = new Button("POINT " + (i + 1));
            bpoint[i].addActionListener(this);
            bpoint[i].setEnabled(false);
            painelBotoes.add(bpoint[i]);
        }
        
        painelBotoes.add(bzerar);
        painelBotoes.add(bsalvar);
        painelBotoes.add(brelatorio);

        
        janela.add(painelTimer);
        janela.add(painelBotoes);
        janela.setVisible(true);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_PRINTSCREEN || e.getKeyCode() == KeyEvent.VK_S) {
                painelTimer.setVisible(false);
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        painelTimer.setVisible(true);
                    }
                });
                timer.setRepeats(false); 
                timer.start();
            }
            return false;
        });
        
    }
    
    public void actionPerformed(ActionEvent e) {
        Button b=(Button)e.getSource();
        if (b==bstart) {
            this.botaoStart();
        } else if (b==bparar) {
            this.botaoParar();
        } else if (b==bzerar) {
            this.botaoFim();
        } else if (b==bsalvar) {
            this.botaoSalvar();
        } else if (b==brelatorio) {
            this.botaoRelatorio();
        } else {
            for (int i = 0; i < quantBotoesPoint; i++) {
                if (b==bpoint[i]) {
                    this.botaoPoint(i);
                }
            }
        }
    }
    
    public static Connection conecta() {
        String url = "jdbc:mysql://localhost:3306/cronometro?useTimezone=true&serverTimezone=UTC";
        Connection con;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "");
            System.out.println("Conectado com sucesso");
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
        bstart.setEnabled(false);
        bparar.setEnabled(true);
        for(int i = 0; i < quantBotoesPoint; i++) {
            bpoint[i].setEnabled(true);
        }
        bzerar.setEnabled(true);
    }
    
    void botaoParar() {
        c.parar();
        timerSwing.stop();
        bstart.setEnabled(true);
        bparar.setEnabled(false);
    }
        
    void botaoPoint(int codEquipe) {
        equipe equipeObject = equipesParticipantes.get(codEquipe);
        String ultVolta = equipeObject.setVolta(c.getTimerInt(), c.getTimer());
        formatoAntes += "Equipe " + equipeObject.getNome() + " - Volta " + equipeObject.getQuantVoltas() + " - " + ultVolta + "\n";
        tvoltas.setText(formatoAntes);

        bsalvar.setEnabled(true);
    }    

    void botaoFim() {
        c.parar();
        c.zerar();
        lcronometro.setText(c.getTimer());
        timerSwing.stop();
        bparar.setEnabled(false);
        for(int i = 0; i < quantBotoesPoint; i++) {
            bpoint[i].setEnabled(false);
        }
        bzerar.setEnabled(false);
        bsalvar.setEnabled(false);
    }
    
    void botaoRelatorio() {
        botaoFim();
        relatorio r  = new relatorio();;
        r.mostrarTela();
    }
    
    void botaoSalvar(){
        Connection con = conecta();
        try {
            int resultado;
            Statement st = con.createStatement();
            for (int i= 0; i < equipesParticipantes.size(); i++) {
                equipe equipeAtual = equipesParticipantes.get(i);
                for (int j = 0; j< equipeAtual.getQuantVoltas(); j++) {
                    resultado = st.executeUpdate("insert into voltas (cod_volta, tempo, cod_equipe, tempo_total) values (" + (j + 1) + ", " + equipeAtual.getVoltaParcial(j) + ", "+ equipeAtual.getCodEquipe() + ", " + equipeAtual.getVolta(j) + ");");
                }
                System.out.println(equipeAtual.getCodEquipe());
                st.executeUpdate("update equipes set tempo_total = " + equipeAtual.getTempoTotal() + " where cod_equipe = " + equipeAtual.getCodEquipe());
            }
            st. close();
            con.close();
        } catch (SQLException sql){
            System.out.println(sql);
        }
        brelatorio.setEnabled(true);
    }
    public void windowClosing(WindowEvent e) {
        System.exit(0);     
    }
        
    public static void main(String[] args) {
        String quantEquipesString = JOptionPane.showInputDialog("Quantas equipes irão participar?");
        switch (quantEquipesString) {
            case "0":
            case "1":
                JOptionPane.showMessageDialog(null, "É necessário ter mais de 1 equipe para continuar");
                break;
            default:
                try {
                    int numero = Integer.parseInt(quantEquipesString);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor digite um número!");
                    break;
                }
                int quantEquipes = Integer.parseInt(quantEquipesString);
                Vector<equipe> equipesParticipantes = new Vector<>();
                for (int i = 0; i < quantEquipes; i++) {
                    String nomeEquipeAdd = JOptionPane.showInputDialog("Insira o nome da equipe " + (i + 1) + ":");
                    equipesParticipantes.add(new equipe(nomeEquipeAdd, i, 0));
                }
                Connection con = conecta();
                try {
                    int resultado;
                    Statement st = con.createStatement();
                    st.executeUpdate("delete from voltas;");
                    st.executeUpdate("delete from equipes;");
                    for (int i = 0; i < equipesParticipantes.size(); i++) {
                        int codEquipe = equipesParticipantes.get(i).getCodEquipe();
                        String nomeEquipe = equipesParticipantes.get(i).getNome();
                        resultado = st.executeUpdate("insert into equipes (cod_equipe, nome_equipe) values (" + codEquipe + ", '" + nomeEquipe + "');");
                    }
                    st.close();
                    con.close();
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }
                tela t = new tela(quantEquipes, equipesParticipantes);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
       
    }
}

