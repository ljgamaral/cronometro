package com.mycompany.cronometro;

import javax.swing.JTable;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.JScrollPane;

public class relatorio extends WindowAdapter implements ActionListener, KeyListener {
    private Frame janela;
    private Panel painelTabela;
    private JTable tabela;
    private Vector<equipe> equipesParticipantes;
    private JScrollPane barraRolagem;
    
    public relatorio () {
        janela = new Frame();
        janela.setTitle("Relatório");
        janela.setSize(370,414);
        janela.setBackground(new Color(160,160,160));
        janela.setLayout(null);
        janela.addWindowListener(this);
        
        painelTabela = new Panel();
        painelTabela.setBackground(new Color(128,128,128));
        painelTabela.setSize(350,234);
        painelTabela.setLocation(10,80);
        painelTabela.setLayout(new BorderLayout());
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_PRINTSCREEN || e.getKeyCode() == KeyEvent.VK_S) {
                painelTabela.setVisible(false);
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        painelTabela.setVisible(true);
                    }
                });
                timer.setRepeats(false); 
                timer.start();
            }
            return false;
        });
        
        String [] colunas = {"Classificação", "Equipe", "Tempo total"};
        Object [][] dados = listarDados();

        tabela = new JTable(dados, colunas);
        tabela.setBounds(25, 50, 100, 100);
        
        barraRolagem = new JScrollPane(tabela);
        painelTabela.add(barraRolagem, BorderLayout.CENTER);
        
        janela.add(painelTabela);      
    }
    
    public Object [][] listarDados () {
        Connection con = tela.conecta();
        Vector<equipe> equipesParticipantes = new Vector<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from equipes order by tempo_total;");
            while(rs.next()) {
                int codEquipe = rs.getInt(1);
                String nomeEquipe = rs.getString(2);
                int tempoTotal = rs.getInt(3);
                equipesParticipantes.add(new equipe(nomeEquipe, codEquipe, tempoTotal));
            }
            st. close();
            con.close();
        } catch (SQLException sql) {
            System.out.println("Erro SQL: " + sql);
        }
        
        Object [][] dados = new Object[equipesParticipantes.size()][3];
        Cronometro c = new Cronometro();
        
        for (int i = 0; i < equipesParticipantes.size(); i++) {
            equipe equipeAtual = equipesParticipantes.get(i);
            dados[i][0] = i+1 + "°";
            dados[i][1] = equipeAtual.getNome();
            dados[i][2] = c.formataMiliSegs(equipeAtual.getTempoTotal());
        }
        
        return dados;
    }
    
    public void mostrarTela() {
        janela.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        
    }
    
    public void windowClosing(WindowEvent e) {
        janela.setVisible(false);   
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
