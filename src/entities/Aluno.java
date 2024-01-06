package entities;
import java.util.List;

public class Aluno extends Pessoa {
 private List<Double> notas;

 public Aluno(String nome, String matricula, List<Double> notas) {
     super(nome, matricula);
     this.notas = notas;
 }

 public List<Double> getNotas() {
     return notas;
 }

 public void visualizarNotas() {
     System.out.println("Notas do aluno " + getNome() + ": " + notas);
 }

 @Override
 public void exibirDetalhes() {
     System.out.println("Aluno: " + getNome() + ", Matrícula: " + getMatricula());
 }
}
