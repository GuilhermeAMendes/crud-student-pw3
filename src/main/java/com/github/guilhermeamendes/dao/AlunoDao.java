package com.github.guilhermeamendes.dao;

import com.github.guilhermeamendes.modelo.Aluno;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AlunoDao {
    private final EntityManager entityManager;

    public AlunoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void cadastrarAluno(Aluno aluno) {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.persist(aluno);
            this.entityManager.getTransaction().commit();
            System.out.println("Aluno cadastrado com sucesso!");
        } catch (Exception e) {
            this.entityManager.getTransaction().rollback();
            System.out.println("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public void excluirAluno(Aluno aluno) {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.remove(aluno);
            this.entityManager.getTransaction().commit();
            System.out.println("Aluno removido com sucesso!");
        } catch (Exception e) {
            this.entityManager.getTransaction().rollback();
            System.out.println("Erro ao remover aluno: " + e.getMessage());
        }
    }

    public void alterarAluno(Aluno aluno) {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.merge(aluno);
            this.entityManager.getTransaction().commit();
            System.out.println("Aluno alterado com sucesso!");
        } catch (Exception e) {
            this.entityManager.getTransaction().rollback();
            System.out.println("Erro ao alterar aluno: " + e.getMessage());
        }
    }

    public Aluno buscarAlunoPeloNome(String nome) {
        try {
            String jpql = "select a from Aluno a where a.nome = :nome";
            return this.entityManager.createQuery(jpql, Aluno.class).setParameter("nome", nome).getResultList().getFirst();
        } catch (Exception e) {
            System.out.println("Aluno não encontrado!");
        }
        return null;
    }

    public void imprimirAluno(Aluno aluno) {
        if (aluno == null) return;
        System.out.println("Dados do aluno: \n" + "Nome:" + aluno.getNome() + "\nEmail: " + aluno.getEmail() +
                "\nRA: " + aluno.getRa() + "\nNotas: " + aluno.getNota1() + "-" + aluno.getNota2() + "-"
                + aluno.getNota3());
    }

    public List<Aluno> listarAlunos() {
        try {
            String jpql = "select a from Aluno a";
            return this.entityManager.createQuery(jpql, Aluno.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
