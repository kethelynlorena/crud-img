package br.itb.projeto.padaria3_imgs.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

// DEFINE A CLASSE COMO UMA ENTIDADE NA CONEXÃO COM O BANCO DE DADOS
@Entity

// INDICA QUAL TABELA DO BANCO DE DADOS REPRESENTA A CLASSE
@Table(name = "Usuario")
public class Usuario {
	
	@Id
	// DEFINE A GERAÇÃO AUTOMÁTICA VALORES PARA O ATRIBUTO
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String nome;
	private String email;
	private String senha;
	private String nivelAcesso; // ADMIN, TECNICO ou USER
	private byte[] foto;
	private String statusUsuario;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getNivelAcesso() {
		return nivelAcesso;
	}
	public void setNivelAcesso(String nivelAcesso) {
		this.nivelAcesso = nivelAcesso;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public String getStatusUsuario() {
		return statusUsuario;
	}
	public void setStatusUsuario(String statusUsuario) {
		this.statusUsuario = statusUsuario;
	}

}