package entities;

import java.util.ArrayList;
import java.util.List;

public class Pessoa {
    protected String nome;
    protected List<String> materias;

    public Pessoa(String nome) {
        this.nome = nome;
        this.materias = new ArrayList<>();
    }

    // Getters and setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getMaterias() {
        return materias;
    }

    public void setMaterias(List<String> materias) {
        this.materias = materias;
    }
}
