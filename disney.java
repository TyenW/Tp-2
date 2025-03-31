import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class Disney {
    // Disney class
    private String show_id;
    private String type;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;

    // Constructor
    public Disney(String show_id, String type, String director, String[] cast, String country, Date date_added, int release_year, String rating, String duration, String[] listed_in) {
        this.show_id = show_id;
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

    // Getters and Setters
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
    
   static void imprimir(Disney[] disney) {
        for (Disney d : disney) {
            System.out.println("Show ID: " + d.getShow_id());
            System.out.println("Type: " + d.getType());
            System.out.println("Director: " + d.getDirector());
            System.out.println("Cast: " + Arrays.toString(d.getCast()));
            System.out.println("Country: " + d.getCountry());
            System.out.println("Date Added: " + d.getDate_added());
            System.out.println("Release Year: " + d.getRelease_year());
            System.out.println("Rating: " + d.getRating());
            System.out.println("Duration: " + d.getDuration());
            System.out.println("Listed In: " + Arrays.toString(d.getListed_in()));
            System.out.println("------------------------------");
        }
        
   }
   public Disney[] ler() throws Exception {
       File file = new File("C:\\Users\\Pedro Guimarães\\Desktop\\codigos\\Tp-2\\disneyplus.csv"); // Ensure the correct file path
       if (!file.exists()) {
           throw new java.io.FileNotFoundException("The file disney.csv was not found at the specified path.");
       }
    Scanner scanner = new Scanner(file);
    Disney[] disneyList = new Disney[1369]; 
// Skip the header line
if (scanner.hasNextLine()) {
    scanner.nextLine();
}
while (scanner.hasNextLine()) {
    String line = scanner.nextLine();
    String[] fields = line.split(",");
    if (fields.length < 10) {
        System.err.println("Skipping malformed line: " + line);
        continue;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); // Adjust format as per your CSV
    Date date_added = null;
    try {
        date_added = sdf.parse(fields[5]);
    } catch (ParseException e) {
        System.err.println("Error parsing date: " + fields[5]);
    }
    int release_year;
    try {
        release_year = Integer.parseInt(fields[6]);
    } catch (NumberFormatException e) {
        System.err.println("Error parsing release year: " + fields[6]);
        release_year = 0; // Default value
    }
    String type = fields[1];
    String director = fields[2];
    String[] cast = fields[3].split(";");
    String country = fields[4];
    String rating = fields[7];
    String duration = fields[8];
    String[] listed_in = fields[9].split(";");

    Disney disney = new Disney(fields[0], type, director, cast, country, date_added, release_year, rating, duration, listed_in);
    for (int i = 0; i < disneyList.length; i++) {
        if (disneyList[i] == null) {
            disneyList[i] = disney; // Add to the first empty slot
            break;
        }
    }



        
    }
    scanner.close();
    return disneyList; // Return the array of Disney objects
    }
    public static void main(String[] args) throws Exception {
        Disney disneyInstance = new Disney(null, null, null, null, null, null, 0, null, null, null); // Create an instance of Disney

        Disney[] disney = disneyInstance.ler(); // Call the ler method to read the CSV file and assign the result
        imprimir(disney); // Call the imprimir method to print the Disney objects
   }





}