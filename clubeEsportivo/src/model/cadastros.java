package src.model;

public class cadastros {
    
    private int nascimento, matricula, idPlano, idPag;
    private String nome, genero;
    private long cpf;
    
    public cadastros(String nome, int nascimento, String genero, long cpf, int idPlano, int idPag) {
        this.nome = nome;
        this.nascimento = nascimento;
        this.genero = genero;
        this.cpf = cpf;
        this.idPlano = idPlano;
        this.idPag = idPag;
    }
    
    public cadastros(String nome, int nascimento, String genero, int matricula, long cpf, int idPlano, int idPag) {
        this.nome = nome;
        this.nascimento = nascimento;
        this.genero = genero;
        this.matricula = matricula;
        this.cpf = cpf;
        this.idPlano = idPlano;
        this.idPag = idPag;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getNascimento() { return nascimento; }
    public void setNascimento(int nascimento) { this.nascimento = nascimento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getMatricula() { return matricula; }
    public void setMatricula(int matricula) { this.matricula = matricula; }

    public long getCpf() { return cpf; }
    public void setCpf(long cpf) { this.cpf = cpf; }

    public int getIdPlano() { return idPlano; }
    public void setIdPlano(int idPlano) { this.idPlano = idPlano; }

    public int getIdPag() { return idPag; }
    public void setIdPag(int idPag) { this.idPag = idPag; }
}
