package entities;

public abstract class Pessoa {
 private String nome;
 private String matricula;

 public Pessoa(String nome, String matricula) {
     this.nome = nome;
     this.matricula = matricula;
 }

 public String getNome() {
     return nome;
 }

 public String getMatricula() {
     return matricula;
 }

 public abstract void exibirDetalhes();
}
