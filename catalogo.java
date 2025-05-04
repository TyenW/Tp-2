import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

 class Disney {
    // Atributos da classe Disney
    private String show_id; // Identificador único do show
    private String title; // Título do show
    private String type; // Tipo do show (ex: Filme, Série)
    private String director; // Nomes do diretores
    private String[] cast; // Lista de atores
    private String country; // País de origem
    private Date date_added; // Data em que foi adicionado ao catálogo
    private int release_year; // Ano de lançamento
    private String rating; // Classificação indicativa
    private String duration; // Duração do show
    private String[] listed_in; // Gêneros ou categorias em que o show está listado
   
    // Construtor 1 (Recebe todos os parâmetros para inicializar os atributos)
    public Disney(String show_id, String title, String type, String director, String[] cast, String country, Date date_added, int release_year, String rating, String duration, String[] listed_in) {
        this.show_id = show_id;
        this.title = title;
        this.type = type;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.date_added = date_added;
        this.release_year = release_year;
        this.rating = rating;
        this.duration = duration;
        this.listed_in = listed_in;
    }

    // Construtor 2 (Inicializa os atributos com valores padrão)
    public Disney() {
        this.show_id = ""; // Identificador vazio
        this.title = ""; // Título vazio
        this.type = ""; // Tipo vazio
        this.director = ""; // Diretor vazio
        this.cast = new String[0]; // Elenco vazio
        this.country = ""; // País vazio
        this.date_added = null; // Data nula
        this.release_year = 0; // Ano de lançamento padrão (0)
        this.rating = ""; // Classificação indicativa vazia
        this.duration = ""; // Duração vazia
        this.listed_in = new String[0]; // Lista de gêneros vazia
    }

    // Métodos Getters e Setters para acessar e modificar os atributos
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getShow_id() {
        return show_id;
    }
    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String[] getCast() {
        return cast;
    }
    public void setCast(String[] cast) {
        this.cast = cast;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Date getDate_added() {
        return date_added;
    }
    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }
    public int getRelease_year() {
        return release_year;
    }
    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String[] getListed_in() {
        return listed_in;
    }
    public void setListed_in(String[] listed_in) {
        this.listed_in = listed_in;
    }

    // Método Clone (Cria uma cópia do objeto atual)
    public Disney clone() {
        return new Disney(
            this.show_id,
            this.title,
            this.type,
            this.director,
            this.cast.clone(), // Clona o array de elenco
            this.country,
            this.date_added != null ? (Date) this.date_added.clone() : null, // Clona a data, se não for nula
            this.release_year,
            this.rating,
            this.duration,
            this.listed_in.clone() // Clona o array de gêneros
        );
    }

  
   
    
    // Método para verificar e retornar uma string ou um valor padrão se estiver vazia
    private String verificarString(String campo, String padrao) {
        return campo.isEmpty() ? padrao : campo;
    }

    // Método para verificar e retornar um array ou um array vazio se estiver vazio
    private String[] verificarArray(String campo) {
        return campo.isEmpty() ? new String[0] : campo.split(", ");
    }

    // Método para verificar e retornar uma data ou null se estiver vazia
    private Date verificarData(String campo) throws ParseException {
        return campo.isEmpty() ? null : new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(campo);
    }

    // Método para verificar e retornar um inteiro ou 0 se estiver vazio
    private int verificarInteiro(String campo) {
        return campo.isEmpty() ? 0 : Integer.parseInt(campo);
    }

    // Método para ler os dados do arquivo CSV e criar objetos Disney
    public Disney[] ler() throws Exception {
        File file = new File("/tmp/disneyplus.csv"); // Caminho do arquivo CSV
        if (!file.exists()) {
            throw new java.io.FileNotFoundException("O arquivo disneyplus.csv não foi encontrado no caminho especificado."); // Exceção se o arquivo não existir
        }
        Scanner scanner = new Scanner(file); // Scanner para ler o arquivo
        Disney[] disneyList = new Disney[1369]; // Array para armazenar os objetos Disney
        // Ignora a linha de cabeçalho
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        int index = 0; // Índice para controlar a posição no array Disney
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine(); // Lê cada linha do arquivo CSV
            String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Divide a linha em campos, considerando aspas

            // Remove aspas dos campos
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].replaceAll("^\"|\"$", "").trim();
            }

            try {
                // Faz o parsing dos campos e cria um objeto Disney
                String show_id = verificarString(tokens[0], "");
                String type = verificarString(tokens[1], "");
                String title = verificarString(tokens[2], "");
                String director = verificarString(tokens[3], "");
                String[] cast = verificarArray(tokens[4]);
                String country = verificarString(tokens[5], "");
                Date date_added = verificarData(tokens[6]);
                int release_year = verificarInteiro(tokens[7]);
                String rating = verificarString(tokens[8], "");
                String duration = verificarString(tokens[9], "");
                String[] listed_in = verificarArray(tokens[10]);

                disneyList[index++] = new Disney(show_id, title, type, director, cast, country, date_added, release_year, rating, duration, listed_in);
            } catch (Exception e) {
                System.err.println("Erro ao processar a linha: " + line); // Mensagem de erro ao processar a linha
                e.printStackTrace();
            }
        }
        scanner.close();
        return disneyList; // Retorna o array de objetos Disney
    }

    // Método principal
 

   
    public Disney lerespecifico(String line){
        Disney instancia = new Disney(); // Cria uma nova instância da classe Disney

        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Divide a linha em campos, considerando aspas

        // Remove aspas dos campos e verifica espaços extras
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replaceAll("^\"|\"$", "").trim(); // Remove aspas e espaços extras
        }

        try {
            // Faz o parsing dos campos e cria um objeto Disney
            String show_id = verificarString(tokens[0].trim(), "");
            String type = verificarString(tokens[1].trim(), "");
            String title = verificarString(tokens[2].trim(), "");
            String director = verificarString(tokens[3].trim(), "");
            String[] cast = verificarArray(tokens[4].trim());
            String country = verificarString(tokens[5].trim(), "");
            Date date_added = verificarData(tokens[6].trim());
            int release_year = verificarInteiro(tokens[7].trim());
            String rating = verificarString(tokens[8].trim(), "");
            String duration = verificarString(tokens[9].trim(), "");
            String[] listed_in = verificarArray(tokens[10].trim());

            instancia = new Disney(show_id, title, type, director, cast, country, date_added, release_year, rating, duration, listed_in);
        } catch (Exception e) {
            System.err.println("Erro ao processar a linha: " + line); // Mensagem de erro ao processar a linha
            e.printStackTrace();
        }

        return instancia; // Retorna a instância criada
    }
    //Método que ordena por Id 
   

}
class catalogo {

    static matriculasequencial matricula = new matriculasequencial(804517); // Inicializa com a matrícula 804517
        // Método para ordenar o array de objetos Disney por ID


    // Método para ordenar o array de objetos Disney por tipo (Movies primeiro, depois TV Shows)
    public static Disney[] ordenarPorTipo(Disney[] disney) {
        Arrays.sort(disney, (d1, d2) -> {
            int typeComparison = d1.getType().compareToIgnoreCase(d2.getType());
            if (typeComparison == 0) {
                return d1.getTitle().compareToIgnoreCase(d2.getTitle());
            }
            return typeComparison;
        });
        return disney;
    }

    static void imprimir(Disney[] disney) {
        for (Disney d : disney) {
            System.out.print("=> " + d.getShow_id() + " ## "); // Imprime o ID do show

            // Inverte a ordem do Título e Tipo
            System.out.print(d.getTitle().replace("\"", "").trim() + " ## "); // Remove aspas e espaços extras do título
            System.out.print(d.getType().isEmpty() ? "NaN" : d.getType().trim() + " ## "); // Verifica tipo vazio

            // Ordena os diretores em ordem alfabética e verifica espaços extras
            String director = d.getDirector();
            if (director != null && !director.isEmpty()) {
                String[] directors = director.split(", ");
                for (int i = 0; i < directors.length; i++) {
                    directors[i] = directors[i].trim(); // Remove espaços extras
                }
                Arrays.sort(directors);
                System.out.print(String.join(", ", directors) + " ## ");
            } else {
                System.out.print("NaN ## "); // Campo vazio
            }

            // Ordena o elenco em ordem alfabética, verifica espaços extras e adiciona colchetes
            String[] cast = d.getCast();
            if (cast != null && cast.length > 0) {
                for (int i = 0; i < cast.length; i++) {
                    cast[i] = cast[i].trim(); // Remove espaços extras no início e no fim
                }
                Arrays.sort(cast);
                for (int i = 0; i < cast.length; i++) {
                    cast[i] = cast[i].replace("\"", ""); // Remove aspas, mas mantém os espaços entre nome e sobrenome
                }
                System.out.print("[" + String.join(", ", cast) + "] ## ");
            } else {
                System.out.print("[NaN] ## "); // Campo vazio com colchetes
            }

            // Imprime o país
            System.out.print((d.getCountry() == null || d.getCountry().isEmpty() ? "NaN" : d.getCountry().trim()) + " ## ");

            // Formata a data sem zero à esquerda
            if (d.getDate_added() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                System.out.print(dateFormat.format(d.getDate_added()) + " ## ");
            } else {
                System.out.print("NaN ## "); // Campo vazio
            }

            // Imprime o ano de lançamento
            System.out.print(d.getRelease_year() + " ## ");

            // Imprime a classificação indicativa
            System.out.print((d.getRating() == null || d.getRating().isEmpty() ? "NaN" : d.getRating().trim()) + " ## ");

            // Imprime a duração
            System.out.print((d.getDuration() == null || d.getDuration().isEmpty() ? "NaN" : d.getDuration().trim()) + " ## ");

            // Ordena os gêneros em ordem alfabética, verifica espaços extras e adiciona colchetes
            String[] listed_in = d.getListed_in();
            if (listed_in != null && listed_in.length > 0) {
                for (int i = 0; i < listed_in.length; i++) {
                    listed_in[i] = listed_in[i].trim(); // Remove espaços extras
                }
                Arrays.sort(listed_in);
                System.out.println("[" + String.join(", ", listed_in) + "] ##");
            } else {
                System.out.println("NaN ##"); // Campo vazio com colchetes
            }
        }
    }

    // Heapsort recursivo (chave primária: Diretor, chave de desempate: Título)
    // Os itens sem diretor serão comparados como "NaN" e ordenados alfabeticamente no final
    public static Disney[] ordenacaoHeap(Disney[] arrayDisney) {
        int tamanhoArray = arrayDisney.length;

        // Constrói o heap (reorganiza o array)
        for (int i = tamanhoArray / 2 - 1; i >= 0; i--) {
            arrayDisney = reorganizarHeap(arrayDisney, tamanhoArray, i);
        }

        // Extrai elementos do heap um por um
        for (int i = tamanhoArray - 1; i > 0; i--) {
            // Move a raiz atual para o final
            arrayDisney = trocar(arrayDisney, 0, i);

            // Chama reorganizarHeap na heap reduzida
            arrayDisney = reorganizarHeap(arrayDisney, i, 0);
        }

        return arrayDisney; // Retorna o array ordenado
    }

    // Reorganiza o heap em torno de um nó específico
    private static Disney[] reorganizarHeap(Disney[] arrayDisney, int tamanhoHeap, int indiceRaiz) {
        int maiorIndice = indiceRaiz; // Inicializa o maior como raiz
        int indiceFilhoEsquerdo = 2 * indiceRaiz + 1; // Índice do filho esquerdo
        int indiceFilhoDireito = 2 * indiceRaiz + 2; // Índice do filho direito

        // Verifica se o filho esquerdo é maior que a raiz
        if (indiceFilhoEsquerdo < tamanhoHeap && comparar(arrayDisney[indiceFilhoEsquerdo], arrayDisney[maiorIndice]) > 0) {
            maiorIndice = indiceFilhoEsquerdo;
        }

        // Verifica se o filho direito é maior que o maior até agora
        if (indiceFilhoDireito < tamanhoHeap && comparar(arrayDisney[indiceFilhoDireito], arrayDisney[maiorIndice]) > 0) {
            maiorIndice = indiceFilhoDireito;
        }

        // Se o maior não for a raiz, realiza a troca
        if (maiorIndice != indiceRaiz) {
            arrayDisney = trocar(arrayDisney, indiceRaiz, maiorIndice);

            // Recursivamente reorganiza a subárvore afetada
            arrayDisney = reorganizarHeap(arrayDisney, tamanhoHeap, maiorIndice);
        }

        return arrayDisney;
    }

    // Método para trocar dois elementos no array
    private static Disney[] trocar(Disney[] arrayDisney, int i, int j) {
        Disney temp = arrayDisney[i];
        arrayDisney[i] = arrayDisney[j];
        arrayDisney[j] = temp;
        return arrayDisney;
    }

    // Método de comparação para o Heapsort
    private static int comparar(Disney disney1, Disney disney2) {
        String diretor1 = disney1.getDirector().isEmpty() ? "NaN" : disney1.getDirector();
        String diretor2 = disney2.getDirector().isEmpty() ? "NaN" : disney2.getDirector();

        // Caso 1: Ambos têm diretor (ou "NaN") → ordena por diretor, depois título
        int cmpDiretor = diretor1.compareToIgnoreCase(diretor2);
        if (cmpDiretor != 0) return cmpDiretor;

        // Caso 2: Ordena por título como critério de desempate
        return disney1.getTitle().compareToIgnoreCase(disney2.getTitle());
    }
    // Método principal
    public static void main(String[] args) throws Exception {
        // Inicializa os métodos
        Scanner ler = new Scanner(System.in); // Scanner para ler a entrada do usuário
        Disney disneyInstance = new Disney(); // Cria uma instância da classe Disney usando o construtor padrão
        File file = new File("/tmp/disneyplus.csv"); // Caminho do arquivo CSV
        //File file = new File("disneyplus.csv"); // Caminho do arquivo CSV
        Disney[] disney = new Disney[1369]; // Array para armazenar os objetos Disney
        int contador = 0; // Contador para controlar a posição no array Disney

        // Cria uma instância da classe matriculasequencial
        
      
        long tempoconjunto = 0;
        String id; // Variável para armazenar o ID do show para alocá-lo
        while (!(id = ler.nextLine()).equals("FIM")) { // Lê o ID do show até que "FIM" seja digitado
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean found = false;

                long tempoInicial = System.nanoTime(); // Marca o tempo inicial
                while ((line = br.readLine()) != null) { // Lê cada linha do arquivo
                    String partes[] = line.split(","); // Divide a linha em partes
                    if (id.equals(partes[0])) { // Verifica se o ID corresponde ao procurado
                        disney[contador] = disneyInstance.lerespecifico(line); // Cria um objeto Disney a partir da linha lida
                        contador++;
                        found = true;
                        break; // Sai do loop após encontrar o objeto
                    }
                }
                long tempoFinal = System.nanoTime(); // Marca o tempo final
                tempoconjunto += (tempoFinal - tempoInicial); // Calcula o tempo total
                if (!found) {
                    System.out.println("ID não encontrado: " + id); // Mensagem caso o ID não seja encontrado
                }
            } catch (IOException e) {
                System.err.println("Erro ao acessar o arquivo: " + e.getMessage()); // Mensagem de erro ao acessar o arquivo
            }
        }
        

        // Remove elementos nulos do array
        disney = Arrays.stream(disney).filter(Objects::nonNull).toArray(Disney[]::new);
        
        
        //inicia o cronometro 
        long tempoInicial = System.nanoTime(); // Marca o tempo inicial
        // Ordena o array de objetos Disney por ID
        disney = ordenacaoHeap(disney); // Chama o método de ordenação Heapsort
        long tempoFinal = System.nanoTime(); // Marca o tempo final
        tempoconjunto += (tempoFinal - tempoInicial); // Calcula o tempo total


        // Ordena o array por tipo (Movies primeiro, depois TV Shows)
        //disney = ordenarPorTipo(disney);

        // Imprime o array ordenado
        imprimir(disney);

        matricula.setTempoTotal(tempoconjunto);

        // Chama o método para gerar o arquivo de log
        matricula.geraraqruivolog(matricula); // Gera o arquivo de log com as informações da matrícula
    }
}
class matriculasequencial {
    private int comparacoes;
    private long tempoTotal;
    private static int matricula;

    public void setTempoTotal(long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public void geraraqruivolog(matriculasequencial i){
        // Método para gerar o arquivo de log
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./804517_heapsort.txt", true))) {
            writer.write(matricula + "\t");
            writer.write(i.getComparacoes() + "\t");
            writer.write(String.valueOf(i.getTempoTotal()));
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }
    public matriculasequencial(int matricula) {
        matriculasequencial.matricula = matricula;
        this.comparacoes = 0;
        this.tempoTotal = 0;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        matriculasequencial.matricula = matricula;
    }
    public int getComparacoes() {
        return comparacoes;
    }
    public void incrementarComparacoes() {
        this.comparacoes++; // Incrementa o contador de comparações
    }
    public long getTempoTotal() {
        return this.tempoTotal; // Retorna o tempo total acumulado
    }
}
