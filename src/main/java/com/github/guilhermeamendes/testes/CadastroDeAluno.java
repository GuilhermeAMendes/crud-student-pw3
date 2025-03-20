package com.github.guilhermeamendes.testes;

import com.github.guilhermeamendes.dao.AlunoDao;
import com.github.guilhermeamendes.modelo.Aluno;
import com.github.guilhermeamendes.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

public class CadastroDeAluno {
    private final Scanner leitorTeclado;
    private final AlunoDao alunoDao;
    private final EntityManager entityManager;

    public CadastroDeAluno() {
        this.leitorTeclado = new Scanner(System.in);
        this.entityManager = JPAUtil.getEntityManager();
        this.alunoDao = new AlunoDao(entityManager);
    }

    public static void main(String[] args) {
        CadastroDeAluno app = new CadastroDeAluno();
        app.executarPrograma();
    }

    private void executarPrograma() {
        int opcao;
        do {
            exibirMenu();
            opcao = leitorTeclado.nextInt();
            leitorTeclado.nextLine();
            processarOpcao(opcao);
        } while (opcao != 6);
        encerrarPrograma();
    }

    private void exibirMenu() {
        System.out.println("\n\n** CADASTRO DE ALUNOS **");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Excluir aluno");
        System.out.println("3 - Alterar aluno");
        System.out.println("4 - Buscar aluno pelo nome");
        System.out.println("5 - Listar alunos (com status aprovação)");
        System.out.println("6 - FIM");
        System.out.print("Digite a opção desejada: ");
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarAluno();
            case 2 -> excluirAluno();
            case 3 -> alterarAluno();
            case 4 -> buscarAlunoPeloNome();
            case 5 -> listarAlunosAprovados();
            case 6 -> System.out.println("Encerrando o programa...");
            default -> System.out.println("Opção inválida!");
        }
    }

    private void cadastrarAluno() {
        System.out.println("\nCADASTRO DE ALUNO:");
        System.out.print("Digite o nome: ");
        String nome = leitorTeclado.nextLine();
        System.out.print("Digite o RA: ");
        String ra = leitorTeclado.nextLine();
        System.out.print("Digite o email: ");
        String email = leitorTeclado.nextLine();
        System.out.print("Digite a Nota 1: ");
        BigDecimal nota1 = leitorTeclado.nextBigDecimal();
        System.out.print("Digite a Nota 2: ");
        BigDecimal nota2 = leitorTeclado.nextBigDecimal();
        System.out.print("Digite a Nota 3: ");
        BigDecimal nota3 = leitorTeclado.nextBigDecimal();

        Aluno novoAluno = new Aluno(nome, ra, email, nota1, nota2, nota3);
        alunoDao.cadastrarAluno(novoAluno);
    }

    private void excluirAluno() {
        System.out.println("\nEXCLUIR ALUNO:");
        System.out.print("Digite o nome: ");
        String nomeExcluir = leitorTeclado.nextLine();
        Aluno alunoExcluir = alunoDao.buscarAlunoPeloNome(nomeExcluir);
        if (alunoExcluir != null) {
            alunoDao.excluirAluno(alunoExcluir);
        }
    }

    private void alterarAluno() {
        System.out.println("\nALTERAR ALUNO:");
        System.out.print("Digite o nome: ");
        String nomeAlterar = leitorTeclado.nextLine();
        Aluno alunoAlterar = alunoDao.buscarAlunoPeloNome(nomeAlterar);

        if (alunoAlterar != null) {
            alunoDao.imprimirAluno(alunoAlterar);
            System.out.println("\nNOVOS DADOS:");
            System.out.print("Digite o nome: ");
            alunoAlterar.setNome(leitorTeclado.nextLine());
            System.out.print("Digite o RA: ");
            alunoAlterar.setRa(leitorTeclado.nextLine());
            System.out.print("Digite o email: ");
            alunoAlterar.setEmail(leitorTeclado.nextLine());
            System.out.print("Digite a Nota 1: ");
            alunoAlterar.setNota1(leitorTeclado.nextBigDecimal());
            System.out.print("Digite a Nota 2: ");
            alunoAlterar.setNota2(leitorTeclado.nextBigDecimal());
            System.out.print("Digite a Nota 3: ");
            alunoAlterar.setNota3(leitorTeclado.nextBigDecimal());

            alunoDao.alterarAluno(alunoAlterar);
        }
    }

    private void buscarAlunoPeloNome() {
        System.out.println("CONSULTAR ALUNO: ");
        System.out.print("\nDigite o nome: ");
        String nomeBusca = leitorTeclado.nextLine();
        Aluno alunoBuscado = alunoDao.buscarAlunoPeloNome(nomeBusca);
        alunoDao.imprimirAluno(alunoBuscado);
    }

    private void listarAlunosAprovados() {
        List<Aluno> alunosAprovados = alunoDao.listarAlunos();
        System.out.println("Exibindo todos os alunos: ");
        for (Aluno a : alunosAprovados) {
            BigDecimal media = (a.getNota1().add(a.getNota2()).add(a.getNota3()))
                    .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

            String status = (media.compareTo(BigDecimal.valueOf(6)) >= 0) ? "Aprovado" :
                    (media.compareTo(BigDecimal.valueOf(4)) >= 0) ? "Recuperação" :
                            "Reprovado";


            System.out.println("\nNome: " + a.getNome() + "\nEmail: " + a.getEmail() + "\nRA: " + a.getRa() +
                    "\nNotas: " + a.getNota1() + " - " + a.getNota2() + " - "
                    + a.getNota3() + "\nMedia: " + media + "\nSituação: " + status + "\n");
        }
    }

    private void encerrarPrograma() {
        leitorTeclado.close();
        entityManager.close();
    }
}
