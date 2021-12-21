package br.com.todolist.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarefa implements Comparable<Tarefa> {

	private long id;
	private String nome;
	private LocalDate dataCriacao;
	private LocalDate dataLimite;
	private LocalDate dataConcluida;
	private String descricao;
	private String comentarios;
	private StatusTarefa status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(LocalDate dataLimite) {
		this.dataLimite = dataLimite;
	}

	public LocalDate getDataConcluida() {
		return dataConcluida;
	}

	public void setDataConcluida(LocalDate dataConcluida) {
		this.dataConcluida = dataConcluida;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public StatusTarefa getStatus() {
		return status;
	}

	public void setStatus(StatusTarefa status) {
		this.status = status;
	}

	public String formatToSave() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getId() + ";");
		builder.append(this.getNome() + ";");
		DateTimeFormatter padraoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		builder.append(this.getDataCriacao().format(padraoData) + ";");
		builder.append(this.getDataLimite().format(padraoData) + ";");
		if (this.getDataConcluida() != null) {
			builder.append(this.getDataConcluida().format(padraoData));
		}
		builder.append(";");
		builder.append(this.getDescricao() + ";");
		builder.append(this.getComentarios() + ";");
		builder.append(this.getStatus().ordinal() + "\n");
		return builder.toString();
	}

	@Override
	public int compareTo(Tarefa o) {
		
			if(this.getStatus() == StatusTarefa.CONCLUIDA && o.getStatus() == StatusTarefa.CONCLUIDA) {
				return this.getDataLimite().compareTo(o.getDataLimite());
			}else if(this.getStatus() == StatusTarefa.CONCLUIDA && o.getStatus() != StatusTarefa.CONCLUIDA) {
				return 1;
			}else if(this.getStatus() != StatusTarefa.CONCLUIDA && o.getStatus() == StatusTarefa.CONCLUIDA) {
				return -1;
			}
			return this.getDataLimite().compareTo(o.getDataLimite());
			
	}

}
