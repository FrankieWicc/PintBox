package MODEL;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Crianca {
    private int id;
    private String nome;
    private int idade;
    private String descricao;
    private LocalDate dataNascimento;
    private LocalDate dataChegada;
    private int idOrfanato;

   
    public Crianca(int id, String nome, int idade, String descricao, LocalDate dataNascimento, LocalDate dataChegada, int idOrfanato) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.descricao = descricao;
        this.dataNascimento = dataNascimento;
        this.dataChegada = dataChegada;
        this.idOrfanato = idOrfanato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public int getIdOrfanato() {
        return idOrfanato;
    }

    public void setIdOrfanato(int idOrfanato) {
        this.idOrfanato = idOrfanato;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("ID: %d | Nome: %s | Idade: %d | Nascimento: %s | Chegada: %s | Orfanato: %d\nDescrição: %s", 
                id, nome, idade, 
                dataNascimento.format(formatter), 
                dataChegada.format(formatter), 
                idOrfanato, descricao);
    }
}