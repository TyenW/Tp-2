import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class Disney {
    // Atributos da classe Disney
    private String show_id; // Identificador único do show
    private String title; // Título do show
    private String type; // Tipo do show (ex: Filme, Série)
    private String director; // Nome do diretor
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

    // Método para imprimir os objetos Disney
    static void imprimir(Disney[] disney) {
        for (Disney d : disney) {
            System.out.print("=> " + d.getShow_id() + " ## "); // Imprime o ID do show
            System.out.print(d.getType() + " ## "); // Imprime o tipo do show
            System.out.print(d.getTitle() + " ## "); // Imprime o título do show
            System.out.print((d.getDirector() == null || d.getDirector().isEmpty() ? "NaN" : d.getDirector()) + " ## "); // Imprime o diretor ou "NaN" se vazio
            System.out.print(Arrays.toString(d.getCast()) + " ## "); // Imprime o elenco
            System.out.print((d.getCountry() == null || d.getCountry().isEmpty() ? "NaN" : d.getCountry()) + " ## "); // Imprime o país ou "NaN" se vazio
            System.out.print((d.getDate_added() == null ? "NaN" : new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(d.getDate_added())) + " ## "); // Imprime a data ou "NaN" se nula
            System.out.print(d.getRelease_year() + " ## "); // Imprime o ano de lançamento
            System.out.print((d.getRating() == null || d.getRating().isEmpty() ? "NaN" : d.getRating()) + " ## "); // Imprime a classificação indicativa ou "NaN" se vazio
            System.out.print(d.getDuration() + " ## "); // Imprime a duração
            System.out.println(Arrays.toString(d.getListed_in())); // Imprime os gêneros
        }
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
        File file = new File("C:\\Users\\Pedro Guimarães\\Desktop\\codigos\\Tp-2\\disneyplus.csv"); // Caminho do arquivo CSV
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
    public static void main(String[] args) throws Exception {
        Disney disneyInstance = new Disney(); // Cria uma instância da classe Disney usando o construtor padrão

        Disney[] disney = disneyInstance.ler(); // Chama o método ler para ler o arquivo CSV e atribui o resultado
        disney = Arrays.stream(disney).filter(Objects::nonNull).toArray(Disney[]::new); // Remove elementos nulos do array
        imprimir(disney); // Chama o método imprimir para exibir os objetos Disney
    }

   
    // Métodos de ordenacao (Seguindo o padrão de ordenação por bolha)
    
    
    //Método que ordena por Id 
    public static Disney[] ordenarPorId(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
            if (disney[j].getShow_id().compareTo(disney[j + 1].getShow_id()) > 0) {
                // Troca os elementos de posição
                Disney temp = disney[j];
                disney[j] = disney[j + 1];
                disney[j + 1] = temp;
            }
            }
        }
        return disney; // Retorna o array ordenado
    }

    // Método que ordena por Titulo
    public static Disney[] ordenarPorTitulo(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getTitle().compareTo(disney[j + 1].getTitle()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }

    // Método que ordena por Tipo
    public static Disney[] ordenarPorTipo(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getType().compareTo(disney[j + 1].getType()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Diretor
    public static Disney[] ordenarPorDiretor(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getDirector().compareTo(disney[j + 1].getDirector()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por País
    public static Disney[] ordenarPorPais(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getCountry().compareTo(disney[j + 1].getCountry()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Data
    public static Disney[] ordenarPorData(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getDate_added().compareTo(disney[j + 1].getDate_added()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Ano de Lançamento
    public static Disney[] ordenarPorAno(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getRelease_year() > disney[j + 1].getRelease_year()) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Classificação
    public static Disney[] ordenarPorClassificacao(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getRating().compareTo(disney[j + 1].getRating()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Duração
    public static Disney[] ordenarPorDuracao(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getDuration().compareTo(disney[j + 1].getDuration()) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Gênero
    public static Disney[] ordenarPorGenero(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getListed_in()[0].compareTo(disney[j + 1].getListed_in()[0]) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    // Método que ordena por Elenco
    public static Disney[] ordenarPorElenco(Disney[] disney) {
        for (int i = 0; i < disney.length - 1; i++) {
            for (int j = 0; j < disney.length - i - 1; j++) {
                if (disney[j].getCast()[0].compareTo(disney[j + 1].getCast()[0]) > 0) {
                    // Troca os elementos de posição
                    Disney temp = disney[j];
                    disney[j] = disney[j + 1];
                    disney[j + 1] = temp;
                }
            }
        }
        return disney; // Retorna o array ordenado
    }
    

}
