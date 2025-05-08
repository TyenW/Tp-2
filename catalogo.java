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
    

    public static void mergeSort(List<Disney> vetor, int esq, int dir, int[] stats) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSort(vetor, esq, meio, stats);
            mergeSort(vetor, meio + 1, dir, stats);

            // Intercalação
            int nEsq = meio - esq + 1;
            int nDir = dir - meio;

            List<Disney> arrayEsq = new ArrayList<>();
            List<Disney> arrayDir = new ArrayList<>();

            for (int i = 0; i < nEsq; i++) {
                arrayEsq.add(vetor.get(esq + i));
            }

            for (int j = 0; j < nDir; j++) {
                arrayDir.add(vetor.get(meio + 1 + j));
            }

            int iEsq = 0, iDir = 0;
            int k = esq;

            while (iEsq < nEsq && iDir < nDir) {
                stats[0]++; // Incrementa comparações
                matricula.incrementarComparacoes(); // Incrementa comparações na matrícula
                Disney left = arrayEsq.get(iEsq);
                Disney right = arrayDir.get(iDir);

                int comp = compare(left, right);

                if (comp <= 0) {
                    vetor.set(k, left);
                    iEsq++;
                } else {
                    vetor.set(k, right);
                    iDir++;
                }
                stats[1]++; // Incrementa movimentações
                k++;
            }

            while (iEsq < nEsq) {
                vetor.set(k++, arrayEsq.get(iEsq++));
                stats[1]++; // Incrementa movimentações
            }

            while (iDir < nDir) {
                vetor.set(k++, arrayDir.get(iDir++));
                stats[1]++; // Incrementa movimentações
            }
        }
    }
    
    private static int compare(Disney a, Disney b) {
        // Comparar por duration (convertido de String para int)
        int compDuration = compareDuration(a.getDuration(), b.getDuration());
        if (compDuration != 0) {
            return compDuration;
        }
    
        // Desempate por title
        return a.getTitle().compareToIgnoreCase(b.getTitle());
    }
    
    private static int compareDuration(String durationA, String durationB) {
        try {
            int durationAInt = Integer.parseInt(durationA);
            int durationBInt = Integer.parseInt(durationB);
            return Integer.compare(durationAInt, durationBInt);
        } catch (NumberFormatException e) {
            // Em caso de erro de conversão, considerar como 0 (ou outro valor padrão)
            return durationA.compareTo(durationB);
        }
    }



    // Método para ordenar o array de objetos Disney por release_year usando Counting Sort
    public static Disney[] countingSortPorAno(Disney[] disney) {
        // Determina o menor e o maior ano de lançamento
        int menorAno = Integer.MAX_VALUE;
        int maiorAno = Integer.MIN_VALUE;

        for (Disney d : disney) {
            if (d.getRelease_year() < menorAno) {
                menorAno = d.getRelease_year();
            }
            if (d.getRelease_year() > maiorAno) {
                maiorAno = d.getRelease_year();
            }
        }

        // Cria o array de contagem
        int range = maiorAno - menorAno + 1;
        int[] count = new int[range];
        Arrays.fill(count, 0);

        // Conta a frequência de cada ano de lançamento
        for (Disney d : disney) {
            count[d.getRelease_year() - menorAno]++;
        }

        // Calcula os índices acumulados
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Cria o array de saída
        Disney[] sortedDisney = new Disney[disney.length];

        // Ordena os objetos Disney por release_year
        for (int i = disney.length - 1; i >= 0; i--) {
            int pos = count[disney[i].getRelease_year() - menorAno] - 1;
            sortedDisney[pos] = disney[i];
            count[disney[i].getRelease_year() - menorAno]--;
        }

        // Resolve o critério de desempate (title) usando Arrays.sort
        int start = 0;
        while (start < sortedDisney.length) {
            int end = start;
            while (end < sortedDisney.length && sortedDisney[start].getRelease_year() == sortedDisney[end].getRelease_year()) {
                end++;
            }
            Arrays.sort(sortedDisney, start, end, Comparator.comparing(Disney::getTitle, String.CASE_INSENSITIVE_ORDER));
            start = end;
        }

        return sortedDisney;
    }
    // Método para ordenar o array de objetos Disney por duração usando MergeSort
    public static Disney[] mergeSortPorDuracao(Disney[] disney) {
        if (disney.length <= 1) {
            return disney;
        }

        int meio = disney.length / 2;

        // Divide o array em duas metades
        Disney[] esquerda = Arrays.copyOfRange(disney, 0, meio);
        Disney[] direita = Arrays.copyOfRange(disney, meio, disney.length);

        // Ordena recursivamente as duas metades
        esquerda = mergeSortPorDuracao(esquerda);
        direita = mergeSortPorDuracao(direita);

        // Combina as duas metades ordenadas
        return merge(esquerda, direita);
    }

    // Método para combinar dois arrays ordenados
    private static Disney[] merge(Disney[] esquerda, Disney[] direita) {
        Disney[] resultado = new Disney[esquerda.length + direita.length];
        int i = 0, j = 0, k = 0;

        while (i < esquerda.length && j < direita.length) {
            // Compara a duração
            int comparacaoDuracao = compararDuracao(esquerda[i].getDuration(), direita[j].getDuration());
            if (comparacaoDuracao < 0 || (comparacaoDuracao == 0 && esquerda[i].getTitle().compareToIgnoreCase(direita[j].getTitle()) <= 0)) {
                resultado[k++] = esquerda[i++];
            } else {
                resultado[k++] = direita[j++];
            }
        }

        // Copia os elementos restantes da metade esquerda, se houver
        while (i < esquerda.length) {
            resultado[k++] = esquerda[i++];
        }

        // Copia os elementos restantes da metade direita, se houver
        while (j < direita.length) {
            resultado[k++] = direita[j++];
        }

        return resultado;
    }

    // Método para comparar a duração (considera "NaN" como maior valor)
    private static int compararDuracao(String duracao1, String duracao2) {
        if (duracao1 == null || duracao1.isEmpty()) {
            duracao1 = "NaN";
        }
        if (duracao2 == null || duracao2.isEmpty()) {
            duracao2 = "NaN";
        }

        // Se ambos forem "NaN", considera iguais
        if (duracao1.equals("NaN") && duracao2.equals("NaN")) {
            return 0;
        }

        // Se apenas um for "NaN", considera o outro menor
        if (duracao1.equals("NaN")) {
            return 1;
        }
        if (duracao2.equals("NaN")) {
            return -1;
        }

        // Extrai os números das durações (assume que estão no formato "X min" ou "X Season(s)")
        int valor1 = extrairValorDuracao(duracao1);
        int valor2 = extrairValorDuracao(duracao2);

        return Integer.compare(valor1, valor2);
    }

    // Método para extrair o valor numérico da duração
    private static int extrairValorDuracao(String duracao) {
        String[] partes = duracao.split(" ");
        try {
            if (partes[1].toLowerCase().contains("season")) {
                return Integer.parseInt(partes[0]) * 60; // Converte temporadas para minutos (1 temporada = 60 minutos)
            } else {
                return Integer.parseInt(partes[0]); // Retorna os minutos diretamente
            }
        } catch (Exception e) {
            return Integer.MAX_VALUE; // Retorna um valor alto para durações inválidas
        }
    }

    // Método para ordenar o array de objetos Disney por seleção e retornar os 10 primeiros elementos
    public static Disney[] selecaoTop10(Disney[] disney) {
        int n = disney.length;

        // Realiza a ordenação por seleção em ordem alfabética
        for (int i = 0; i < Math.min(10, n); i++) {
            int menorIndice = i;
            for (int j = i + 1; j < n; j++) {
                // Compara os elementos pelo título em ordem alfabética
                if (disney[j].getTitle().compareToIgnoreCase(disney[menorIndice].getTitle()) < 0) {
                    menorIndice = j;
                }
            }

            // Troca os elementos, colocando o menor no início
            Disney temp = disney[i];
            disney[i] = disney[menorIndice];
            disney[menorIndice] = temp;
        }

        // Retorna apenas os 10 primeiros elementos em ordem alfabética
        return Arrays.copyOfRange(disney, 0, Math.min(10, n));
    }

    // Método para ordenar o array de objetos Disney por Quicksort com chave date_added e critério de desempate title, retornando apenas o top 10
    public static Disney[] quicksortTop10(Disney[] disney) {
        quicksort(disney, 0, disney.length - 1);

        // Retorna apenas os 10 primeiros elementos
        return Arrays.copyOfRange(disney, 0, Math.min(10, disney.length));
    }

    // Método Quicksort
    private static void quicksort(Disney[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);

            // Ordena recursivamente as subpartes
            quicksort(array, low, pi - 1);
            quicksort(array, pi + 1, high);
        }
    }

    // Método para particionar o array
    private static int partition(Disney[] array, int low, int high) {
        Disney pivot = array[high]; // Pivô
        int i = low - 1; // Índice do menor elemento

        for (int j = low; j < high; j++) {
            // Compara os elementos pela data adicionada
            int dateComparison = compareDates(array[j].getDate_added(), pivot.getDate_added());
            if (dateComparison < 0 || (dateComparison == 0 && array[j].getTitle().compareToIgnoreCase(pivot.getTitle()) < 0)) {
                i++;
                // Troca os elementos
                Disney temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Troca o pivô com o elemento na posição correta
        Disney temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1; // Retorna o índice do pivô
    }

    // Método para comparar duas datas (considera null como menor valor)
    private static int compareDates(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
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
<<<<<<< HEAD
       disney = quicksortTop10(disney);// Ordena o array por ano de lançamento
=======
        int[] stats = new int[2]; // Array para armazenar comparações e movimentações
        mergeSort(Arrays.asList(disney), 0, disney.length - 1, stats); // Converte o array para uma lista e chama o método MergeSort
>>>>>>> 27d0b4c31507183e0551229b28d63969222b7bff
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./804517_quicksortparcial.txt", true))) {
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
