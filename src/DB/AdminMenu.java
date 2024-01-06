package DB;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminMenu {
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		Scanner sc = new Scanner(System.in);
		Conexao con = new Conexao();
		int menu = 0;
		while (menu != 99) {
			System.out.println("Selecione uma opção:");
			System.out.println("8 - RESET (exclue tudo da DB)");
			System.out.println("99 - Sair");
			menu = sc.nextInt();

			switch (menu) {

			case 8:
				System.out.println("Tem certeza que deseja excluir todos os professores e alunos? (1-Sim / 2-Não)");
				int confirmacao = sc.nextInt();
				if (confirmacao == 1) {
					con.excluirTodosProfessoresAlunos();
					System.out.println("Todos os professores e alunos foram excluídos.");
				} else {
					System.out.println("Operação cancelada.");
				}
				break;

			case 99:
				break;

			default:
				System.out.println("Opção inválida.");
				break;
			}
		}

		sc.close();
	}
}
