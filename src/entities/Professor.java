package entities;

public class Professor extends Pessoa {
 private String disciplina;

 public Professor(String nome, String matricula, String disciplina) {
     super(nome, matricula);
     this.disciplina = disciplina;
 }

 public String getDisciplina() {
     return disciplina;
 }

 public void atribuirNota(Aluno aluno, double nota) {
     aluno.getNotas().add(nota);
     System.out.println("Nota atribuída ao aluno " + aluno.getNome() +
             " na disciplina " + disciplina + ": " + nota);
 }

 @Override
 public void exibirDetalhes() {
     System.out.println("Professor: " + getNome() + ", Matrícula: " + getMatricula() +
             ", Disciplina: " + disciplina);
 }
}
