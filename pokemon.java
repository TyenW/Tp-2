import java.util.ArrayList;
import java.util.Date;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class pokemon {
    private int id;
    private int geracao;
    private String nome;
    private String descricao; // Correção aqui
    private ArrayList<String> tipos;
    private ArrayList<String> habilidades; // Correção aqui
    private double peso;
    private double altura;
    private int chanceCaptura; // Correção aqui
    private boolean lendario;
    private Date dia;
     
    public void setHabilidades(ArrayList<String> Habilidades) {
        this.habilidades = Habilidades;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public void setChance_captura(int chance_captura) {
        this.chanceCaptura = chance_captura;
    }
    public void setDescicao(String descicao) {
        this.descricao = descicao;
    }
    public void setDia(Date dia) {
        this.dia = dia;
    }
    public void setGeracao(int geracao) {
        this.geracao = geracao;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setLendario(boolean lendario) {
        this.lendario = lendario;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }
    public void setTipos(ArrayList<String> tipos) {
        this.tipos = tipos;
    }
    public ArrayList<String> getHabilidades() {
        return habilidades;
    }
    public double getAltura() {
        return altura;
    }
    public int getChance_captura() {
        return chanceCaptura;
    }
    public String getDescicao() {
        return descricao;
    }
    public Date getDia() {
        return dia;
    }
    public ArrayList<String> getTipos() {
        return tipos;
    }
    public int getGeracao() {
        return geracao;
    }
    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public double getPeso() {
        return peso;
    }

    public pokemon(int id, int geracao, String nome, String descricao, ArrayList<String> tipos, 
                   ArrayList<String> habilidades, double peso, double altura, 
                   int chanceCaptura, boolean lendario, Date dia) {
        this.id = id;
        this.geracao = geracao;
        this.nome = nome;
        this.descricao = descricao;
        this.tipos = tipos;
        this.habilidades = habilidades;
        this.peso = peso;
        this.altura = altura;
        this.chanceCaptura = chanceCaptura; // Correção aqui
        this.lendario = lendario;
        this.dia = dia;
    }

    public static ArrayList<pokemon> ler() {
        ArrayList<pokemon> lista = new ArrayList<>();
        String caminho = "C:/Users/Pedro Guimarães/Desktop/Codigos/Tp 2/pokemon.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine(); // Pular cabeçalho
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Divide levando em conta aspas

                ArrayList<String> tipos = new ArrayList<>();
                tipos.add(dados[4].trim());
                if (!dados[5].trim().isEmpty()) {
                    tipos.add(dados[5].trim());
                }

                ArrayList<String> habilidades = new ArrayList<>();
                habilidades.add(dados[6].trim().replaceAll("[\\[\\]']", "")); // Remove colchetes e aspas

                int id = Integer.parseInt(dados[0].trim());
                int geracao = Integer.parseInt(dados[1].trim());
                String nome = dados[2].trim();
                String descricao = dados[3].trim();
                double peso = Double.parseDouble(dados[7].trim());
                double altura = Double.parseDouble(dados[8].trim());
                int chanceCaptura = Integer.parseInt(dados[9].trim());
                boolean lendario = Integer.parseInt(dados[10].trim()) == 1; // Assumindo 1 para lendário
                Date dia = formatoData.parse(dados[11].trim());

                lista.add(new pokemon(id, geracao, nome, descricao, tipos, habilidades, peso, altura, chanceCaptura, lendario, dia));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        
        return lista; // Retorno da lista
    }

    public static void main(String[] args) {
        ArrayList<pokemon> pokemons = ler();
        for (pokemon p : pokemons) {
            System.out.println("ID: " + p.id + ", Nome: " + p.nome); // Acesso direto aos campos para depuração
        }
    }
}

