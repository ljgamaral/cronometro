package com.mycompany.cronometro;

import java.util.Vector;

public class equipe {
    private String nome;
    private int codEquipe;
    private Vector<Integer> voltasMiliSeg;
    private Vector<String> vVoltas;
    private int tempoTotal = 0;
    
    public equipe (String nome, int codEquipe, int tempoTotal) {
        this.nome = nome;
        this.codEquipe = codEquipe;
        this.vVoltas = new Vector<>();
        this.voltasMiliSeg = new Vector<>();
        if (tempoTotal != 0) {
           this.tempoTotal = tempoTotal;
        }
    }
    
    public String getNome() {
        return nome;
    }
    
    public int getCodEquipe() {
        return codEquipe;
    }
    
    public int getQuantVoltas() {
        return vVoltas.size();
    }
    
    public int getTempoTotal() {
        for (int i = 0; i < voltasMiliSeg.size(); i++) {
            tempoTotal += voltasMiliSeg.get(i);
        }
        return tempoTotal;
    }
    
    public int getVolta(int posicao) {
        return voltasMiliSeg.get(posicao);
    }
    
    public int getVoltaParcial(int posicao) {
        if (posicao == 0) {
            return voltasMiliSeg.get(posicao);
        } 
        
        int tempoParcial = voltasMiliSeg.get(posicao) - voltasMiliSeg.get(posicao - 1);
        return tempoParcial;
    }
    
    public String setVolta(int voltaTempoInt, String voltaTempo) {
        vVoltas.add(voltaTempo);
        voltasMiliSeg.add(voltaTempoInt);
        return voltaTempo;
    }
}
