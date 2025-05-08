import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.*;
import java.nio.charset.*;
import java.util.Locale;
import java.util.Collections;


class Main{
    public static void main (String [] args){

        long inicio = System.nanoTime(); // início da contagem do tempo
        Scanner scanProvisorio = new Scanner(System.in);
        String LookingId = "";
        String entrada = new String();
        List<Show> catalogo = ler.readAllFile("/tmp/disneyplus.csv");
        Q13 q13 = new Q13();
        List<Show> lista = new ArrayList<Show>();

        while (!(entrada = scanProvisorio.nextLine()).equals("FIM")) {
            LookingId = entrada;
            lista.add(ShowSearch.ShowSearchId(catalogo, LookingId));
        }

        q13.MergeSort(lista, 0, lista.size()-1);

        for (int i = 0; i < lista.size(); i++) {
            Show.imprimir(lista.get(i));
        }

        scanProvisorio.close();

        long fim = System.nanoTime(); // fim do tempo
        double tempoExecucao = (fim - inicio) / 1e6; // tempo em milissegundos

        // Criar o arquivo de log
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("0860668_mergesort.txt"))) {
            writer.write("0860668\t" + String.format(Locale.US, "%.3f", tempoExecucao) + "\t" +
                    q13.comparacoes + "\t" + q13.movimentacoes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
}
class Q13 {

    public static int comparacoes = 0;
    public static int movimentacoes = 0;

    public static void MergeSort(List<Show> vetor, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            MergeSort(vetor, esq, meio);
            MergeSort(vetor, meio + 1, dir);
            intercalar(vetor, esq, meio, dir);
        }
    }

    private static void intercalar(List<Show> vetor, int esq, int meio, int dir) {
        int nEsq = meio - esq + 1;
        int nDir = dir - meio;

        List<Show> arrayEsq = new ArrayList<>();
        List<Show> arrayDir = new ArrayList<>();

        for (int i = 0; i < nEsq; i++) {
            arrayEsq.add(vetor.get(esq + i));
        }

        for (int j = 0; j < nDir; j++) {
            arrayDir.add(vetor.get(meio + 1 + j));
        }

        int iEsq = 0, iDir = 0;
        int k = esq;

        while (iEsq < nEsq && iDir < nDir) {
            comparacoes++;
            Show left = arrayEsq.get(iEsq);
            Show right = arrayDir.get(iDir);

            int comp = compare(left, right);

            if (comp <= 0) {
                vetor.set(k, left);
                iEsq++;
            } else {
                vetor.set(k, right);
                iDir++;
            }
            movimentacoes++;
            k++;
        }

        while (iEsq < nEsq) {
            vetor.set(k++, arrayEsq.get(iEsq++));
            movimentacoes++;
        }

        while (iDir < nDir) {
            vetor.set(k++, arrayDir.get(iDir++));
            movimentacoes++;
        }
    }

    private static int compare(Show a, Show b) {
        // Comparar por duration (convertido de String para int)
        int compDuration = compareDuration(a.getDuration(), b.getDuration());
        if (compDuration != 0) {
            return compDuration;
        }

        // Desempate por title
        return a.getTitle().compareTo(b.getTitle());
    }

    // Método auxiliar para comparar duration (String para int)
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
}

/*
--------------
Classe Show
--------------
*/
class Show{
   private String id;
   private String type;
   private String title;
   private ArrayList<String> director;
   private ArrayList<String> cast;
   private String country;
   private Date date_added;
   private int realease_year;
   private String rating; 
   private String duration;
   private ArrayList<String> listed_in;
   

   public Show(){  
      this.id = "";
      this.type = "";
      this.title = "";
      this.director = new ArrayList<String>();
      this.cast = new ArrayList<String>();
      this.country = "";
      this.date_added = new Date();
      this.realease_year = 0;
      this.rating = "";
      this.duration = "";
      this.listed_in = new ArrayList<String>();
   }
    
   public Show(String id, String type, String title, ArrayList<String> director, ArrayList<String> cast, 
                  String contry, Date date_added, int realease_year, String rating, 
                  String duration, ArrayList<String> listed_in){
      this.id = id;
      this.type = type;
      this.title = title;
      this.director = director;
      this.cast = cast;
      this.country = contry;
      this.date_added = date_added;
      this.realease_year = realease_year;
      this.rating = rating;
      this.duration = duration;
      this.listed_in = listed_in;
   }

   public Show(String[] space) throws Exception {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
  
      this.id = space[0].isEmpty() ? "NaN" : space[0];
      this.type = space[1].isEmpty() ? "NaN" : space[1];
      this.title = space[2].isEmpty() ? "NaN" : space[2].replace("\"\"", "\"").replace("\"", "").trim();
  
      // Diretor (campo 3)
      this.director = new ArrayList<String>();
      if (space[3].isEmpty() || space[3].equals("NaN")) {
          this.director.add("NaN");
      } else {
          for (String d : space[3].split(",")) {
              this.director.add(d.trim());
          }
      }
  
      // Elenco (campo 4)
      this.cast = new ArrayList<String>();
      if (space[4].isEmpty() || space[4].equals("NaN")) {
          this.cast.add("NaN");
      } else {
          for (String c : space[4].split(",")) {
              this.cast.add(c.trim());
          }
      }
  
      // País (campo 5)
      this.country = space[5].isEmpty() ? "NaN" : space[5];
  
      // Data (campo 6)
      if (space[6].isEmpty() || space[6].equals("NaN")) {
         this.date_added = null;
      } else {
         String cleanDate = space[6].replace("\"", "").trim(); // remove aspas duplas extras
         this.date_added = dateFormat.parse(cleanDate);
      }
  
      // Ano de lançamento (campo 7)
      this.realease_year = space[7].isEmpty() || space[7].equals("NaN") ? 0 : Integer.parseInt(space[7]);
  
      // Classificação (campo 8)
      this.rating = space[8].isEmpty() ? "NaN" : space[8];
  
      // Duração (campo 9)
      this.duration = space[9].isEmpty() ? "NaN" : space[9];
  
      // Gêneros (campo 10)
      this.listed_in = new ArrayList<String>();
      if (space[10].isEmpty() || space[10].equals("NaN")) {
          this.listed_in.add("NaN");
      } else {
          for (String g : space[10].split(",")) {
              this.listed_in.add(g.trim());
          }
      }
  }
  
   public Show clone (){
      Show tmp = new Show();
      tmp.id = this.id;
      return tmp;
   }

   /*
   Método para imprimir o resultado
   */
   public static void imprimir(Show show) {
      SimpleDateFormat formatoSaida = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
      String data = "NaN";

      if (show.date_added != null) {
         data = formatoSaida.format(show.date_added);
      }

      // Formatando diretor (sem colchetes se for único ou "NaN")
      String diretorFormatado = "NaN";
      if (show.director.size() > 0) {
         if (show.director.size() == 1 && show.director.get(0).equals("NaN")) {
               diretorFormatado = "NaN";
         } else {
               diretorFormatado = String.join(", ", show.director);  // Não há ordenação, mantém a ordem original
         }
      }

      // Formatando cast (com colchetes se estiverem presentes)
      String castFormatado = "[NaN]";
      if (show.cast.size() > 0) {
         // Ordenando a lista de cast em ordem alfabética
         Collections.sort(show.cast);
         castFormatado = "[" + String.join(", ", show.cast) + "]"; // Formata como [a, b, c]
      }

      // Formatando listed_in (com colchetes se estiverem presentes)
      String listedInFormatado = "[NaN]";
      if (show.listed_in.size() > 0) {
         // Ordenando a lista de genres (listed_in) em ordem alfabética
         Collections.sort(show.listed_in);
         listedInFormatado = "[" + String.join(", ", show.listed_in) + "]";  // Formata como [a, b, c]
      }

      // Impressão final
      System.out.println("=> " + show.id + " ## " + show.title + " ## " + show.type + " ## "
               + diretorFormatado + " ## " + castFormatado + " ## " + show.country + " ## " + data
               + " ## " + show.realease_year + " ## " + show.rating + " ## " + show.duration
               + " ## " + listedInFormatado + " ##");
   }

     
    /*
    Definindo metodos getters e setters para cada atributo.
    */
    public String getId(){
      return id;
    }
    public void setId(String id){
      this.id = id;
    }
    public String getType(){
      return type;
    }
    public void setType(String type){
      this.type = type;
    }
    public String getTitle(){
      return title;
    }
    public void setTitle(String title){
      this.title = title;
    }
    public ArrayList<String> getDirector() {
      return director;
    }
    public void setDirector(ArrayList<String> director){
      this.director = director;
    }
    public ArrayList<String> getCast(){
      return cast;
    }
    public void setCast(ArrayList<String> cast){
      this.cast = cast;
    }
    public String getCountry(){
      return country;
    }
    public void setCountry(String country){
      this.country=country;
    }
    public Date getDateAdded() {
        return date_added;
    }
    public void setDateAdded(Date dateAdded) {
        this.date_added = dateAdded;
    }
    public int getRealeaseYear() {
        return realease_year;
    }
    public void setRealeaseYear(int realease_year) {
        this.realease_year = realease_year;
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
    public ArrayList<String> getListedIn() {
        return listed_in;
    }
    public void setListedIn(ArrayList<String> listed_in) {
        this.listed_in = listed_in;
    }
    /*
    Fim da definição dos métodos
    */
}

class ler{
    public static List<Show> readAllFile(final String fileName)
    {
        List<Show> personagens = new ArrayList<Show>();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            // Pular o cabecalho
            br.readLine();

            // Ler linha por linha
            String linha = new String();
            while((linha = br.readLine()) != null)
            {
                // Tratar a linha, para conseguir fazer o plit em ; sem atrapalhar a lista
                    linha = lineFormat(linha);

                // Para cada linha, damos split e jogamos para o construtor, e adicionamos a instancia toda preenchida no nosso array
                Show pessoa = new Show(linha.split(";"));
                personagens.add(pessoa);
            }

            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            personagens = null;
        }

        return personagens;
    }

    // Subistituir todas as , por ; para dar split (menos as , dentro de [])
    private static String lineFormat(String line)
   {
    char[] array_aux = line.toCharArray();
    boolean insideQuotes = false;

    for (int i = 0; i < array_aux.length; i++)
    {
        if (array_aux[i] == '"') {
            insideQuotes = !insideQuotes;
        } else if (array_aux[i] == ',' && !insideQuotes) {
            array_aux[i] = ';';  // só troca se estiver fora das aspas
        }
    }

    return new String(array_aux).replace("\"", "");
   }

}

class ShowSearch {
    // Função estática que busca um Pokémon pelo ID
    public static Show ShowSearchId(List<Show> shows, String id)
    {
        for (Show show : shows)
        {
            if (show.getId().equals(id))
            {
                return show;
            }
        }
        return null;
    }
}