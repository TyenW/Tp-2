import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class pokemon {
    private int id;
    private int geracao;
    private String nome;
    private String descricao; 
    private ArrayList<String> tipos;
    private ArrayList<String> habilidades; 
    private double peso;
    private double altura;
    private int chanceCaptura; 
    private boolean lendario;
    private Date dia;
     
    public void setHabilidades(ArrayList<String> habilidades) {
        this.habilidades = habilidades;
    }
    public void setAltura(double altura) {
        this.altura = altura;
    }
    public void setChanceCaptura(int chanceCaptura) {
        this.chanceCaptura = chanceCaptura;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
    public int getChanceCaptura() {
        return chanceCaptura;
    }
    public String getDescricao() {
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
    public boolean isLendario() {
        return lendario;
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
    this.chanceCaptura = chanceCaptura;
    this.lendario = lendario;
    this.dia = dia;
}

    public static ArrayList<pokemon> ler() {
        ArrayList<pokemon> lista = new ArrayList<>();
        String caminho = "/tmp/pokemon.csv"; 
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine(); 
            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    
                if (dados.length < 12) {
                    System.out.println("Linha com dados insuficientes: " + linha);
                    continue;
                }
    
                ArrayList<String> tipos = new ArrayList<>();
                tipos.add(dados[4].trim());
                if (!dados[5].trim().isEmpty()) {
                    tipos.add(dados[5].trim());
                }
    
                ArrayList<String> habilidades = new ArrayList<>();
                habilidades.add(dados[6].trim().replaceAll("[\\[\\]']", ""));
    
                try {
                    int id = Integer.parseInt(dados[0].trim());
                    int geracao = Integer.parseInt(dados[1].trim());
                    String nome = dados[2].trim();
                    String descricao = dados[3].trim();
                    double peso = dados[7].trim().isEmpty() ? 0 : Double.parseDouble(dados[7].trim());
                    double altura = dados[8].trim().isEmpty() ? 0 : Double.parseDouble(dados[8].trim());
                    int chanceCaptura = dados[9].trim().isEmpty() ? 0 : Integer.parseInt(dados[9].trim());
                    boolean lendario = dados[10].trim().isEmpty() ? false : Integer.parseInt(dados[10].trim()) == 1;
                    Date dia = formatoData.parse(dados[11].trim());
    
                    lista.add(new pokemon(id, geracao, nome, descricao, tipos, habilidades, peso, altura, chanceCaptura, lendario, dia));
                } catch (NumberFormatException | ParseException e) {
                    System.out.println("Erro ao processar a linha: " + linha);
                    System.out.println("Detalhes do erro: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return lista; 
    }

    // Método exibir que substitui o print no maiSn
    public static void exibir(ArrayList<pokemon> pokemons, int num) {
        if (num >= 0 && num < pokemons.size()) {
            pokemon p = pokemons.get(num);
            String tipos = p.getTipos().toString().replace("[", "['").replace("]", "']").replace(", ", "', '");
            String habilidades = p.getHabilidades().toString().replace("[", "['").replace("]", "']").replace("\"", "").replace(", ", "', '");
            String diaFormatado = new SimpleDateFormat("dd/MM/yyyy").format(p.getDia()); 

            String saida = String.format(
                "[#%d -> %s: %s - %s - %s - %.1fkg - %.1fm - %d%% - %b - %d gen] - %s",
                p.getId(), p.getNome(), p.getDescricao(), tipos, habilidades,
                p.getPeso(), p.getAltura(), p.getChanceCaptura(), p.isLendario(), p.getGeracao(),
                diaFormatado
            );
            System.out.println(saida);
        } 
    }

    // Métodos de pesquisa
    public static int pesquisarPorId(ArrayList<pokemon> pokemons, int id) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getId() == id) {
                return i; // Retorna a posição do Pokémon
            }
        
        }
        return -1; // Retorna -1 se não encontrar
    }
    
    public static int pesquisarPorNome(ArrayList<pokemon> pokemons, String nome) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getNome().equalsIgnoreCase(nome)) {
                return i; // Retorna a posição do Pokémon
            }
        }
        return -1; // Retorna -1 se não encontrar
    }
    
    public static int pesquisarPorTipo(ArrayList<pokemon> pokemons, String tipo) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getTipos().contains(tipo)) {
                return i; // Retorna a posição do Pokémon
            }
        }
        return -1; // Retorna -1 se não encontrar
    }

    // Método clone
    public static pokemon clone(ArrayList<pokemon> pokemons, int indice) {
        if (indice >= 0 && indice < pokemons.size()) {
            // Retorna uma nova instância de Pokemon com os mesmos dados
            pokemon original = pokemons.get(indice);
            return new pokemon(
                original.getId(),
                original.getGeracao(),
                original.getNome(),
                original.getDescricao(),
                new ArrayList<>(original.getTipos()), // Clona a lista de tipos
                new ArrayList<>(original.getHabilidades()), // Clona a lista de habilidades
                original.getPeso(),
                original.getAltura(),
                original.getChanceCaptura(),
                original.isLendario(),
                original.getDia() // Presumindo que a data não precisa ser clonada
            );
        } 
        else {
            System.out.println("Índice inválido: " + indice);
            return null; // Retorna null se o índice for inválido
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<pokemon> pokemons = ler();
        ArrayList<pokemon> selctpokemons = new ArrayList<>(); // Inicializando a lista
        String n = scan.nextLine();
        String nome;
        while (!n.equalsIgnoreCase("FIM")) {
            try {
                int num = Integer.parseInt(n) - 1;  // Ajustando para índice zero
                pokemon clonedPokemon = clone(pokemons, num);
                if (clonedPokemon != null) { // Verificando se o clone não é nulo
                    selctpokemons.add(clonedPokemon);
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número ou 'FIM' para encerrar.");
            }
    
            n = scan.nextLine();
        }
        int ids;
        nome =scan.nextLine();
        while(!nome.equalsIgnoreCase("FIM")){
          ids=pesquisarPorNome(selctpokemons, nome);
            if(ids!=-1){
                System.out.println("SIM");
            }
            else{
                System.out.println("NAO");
            }
            nome =scan.nextLine();
        }
    
        scan.close();
    }
}    