import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LucianMovieImporter {
    class Movie implements Comparable<Movie> {
       private String title;
       private float rating;
       private Date release_date;

       public Movie(String title, float rating, Date release_date) {
           this.title = title;
           this.rating = rating;
           this.release_date = release_date;
       }

        @Override
        public int compareTo(Movie m) {
           if(release_date.after(m.release_date))
               return 1;
           else if(release_date.before(m.release_date))
               return -1;
           else {
               return title.compareTo(m.title);
           }
        }

        @Override
        public String toString() {
            return "Title: " + title + " Rating: " + rating + " Release date: " + release_date + "\n";
        }
    }

    static class CSVUtils {

        private static final char DEFAULT_SEPARATOR = ',';
        private static final char DEFAULT_QUOTE = '"';

        public static List<String> parseLine(String cvsLine) {
            return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
        }

        public static List<String> parseLine(String cvsLine, char separators) {
            return parseLine(cvsLine, separators, DEFAULT_QUOTE);
        }

        public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

            List<String> result = new ArrayList<>();

            //if empty, return!
            if (cvsLine == null && cvsLine.isEmpty()) {
                return result;
            }

            if (customQuote == ' ') {
                customQuote = DEFAULT_QUOTE;
            }

            if (separators == ' ') {
                separators = DEFAULT_SEPARATOR;
            }

            StringBuffer curVal = new StringBuffer();
            boolean inQuotes = false;
            boolean startCollectChar = false;
            boolean doubleQuotesInColumn = false;

            char[] chars = cvsLine.toCharArray();

            for (char ch : chars) {

                if (inQuotes) {
                    startCollectChar = true;
                    if (ch == customQuote) {
                        inQuotes = false;
                        doubleQuotesInColumn = false;
                    } else {

                        //Fixed : allow "" in custom quote enclosed
                        if (ch == '\"') {
                            if (!doubleQuotesInColumn) {
                                curVal.append(ch);
                                doubleQuotesInColumn = true;
                            }
                        } else {
                            curVal.append(ch);
                        }

                    }
                } else {
                    if (ch == customQuote) {

                        inQuotes = true;

                        //Fixed : allow "" in empty quote enclosed
                        if (chars[0] != '"' && customQuote == '\"') {
                            curVal.append('"');
                        }

                        //double quotes in column will hit this!
                        if (startCollectChar) {
                            curVal.append('"');
                        }

                    } else if (ch == separators) {

                        result.add(curVal.toString());

                        curVal = new StringBuffer();
                        startCollectChar = false;

                    } else if (ch == '\r') {
                        //ignore LF characters
                        continue;
                    } else if (ch == '\n') {
                        //the end, break!
                        break;
                    } else {
                        curVal.append(ch);
                    }
                }

            }

            result.add(curVal.toString());

            return result;
        }

    }

    public void listMovies(String filePath) throws FileNotFoundException, ParseException {
        Scanner scanner = new Scanner(new File(filePath));
        scanner.nextLine(); //Skipping the first line, which contains the column names
        List<Movie> movieList = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy"); //Date format used in date parsing

        while (scanner.hasNext()) {
            List<String> line = CSVUtils.parseLine(scanner.nextLine());
            String title = line.get(5); //Getting the movie title
            String ratingString = line.get(8); //Getting the movie's score
            float rating = Float.parseFloat(ratingString);
            String dateString = line.get(13); //Getting the date of the movie
            Date date = format.parse(dateString); //Formating the date from string to date
            if(title.charAt(0) == '"') {
                title = title.substring(1);
            } //If the title was formated in case there was a coma in it we remove the " from the beginning
            Movie current_movie = new Movie(title, rating, date); //Creating the movie
            movieList.add(current_movie); //Adding the movie to the list
        }

        Collections.sort(movieList); //Sorting the movies
        System.out.println(movieList);
    }

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        String path = "C:\\Users\\ilies\\OneDrive\\Desktop\\WATCHLIST.csv";
        LucianMovieImporter app = new LucianMovieImporter();
        app.listMovies(path);
    }

}