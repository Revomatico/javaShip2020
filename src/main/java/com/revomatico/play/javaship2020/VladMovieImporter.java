package com.revomatico.play.javaship2020;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import com.revomatico.play.javaship2020.PopcornApp.Movie;

public class VladMovieImporter {

    public void importmovies(PopcornApp ap) {
        String path = "./src/main/resources/WATCHLIST.csv";
        try {
            Scanner buff = new Scanner(new File(path));
            String entire_line = buff.nextLine();// we have column name on the first row
            entire_line = buff.nextLine();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd"); // Date format used in date parsing

            while (buff.hasNextLine()) {

                String[] arrSplit = entire_line.split(",");
                // we have the entire line read in a String, we need to parse it
                String string_year = arrSplit[arrSplit.length - 2];
                try {
                    Date year = format.parse(string_year);
                    String name = arrSplit[5];
                    ap.addMovie(new PopcornApp.Movie(name, year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                entire_line = buff.nextLine();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * public void sortlist(PopcornApp ap) { List<Movie> movies=ap.listMovies();
     * Collection.sort(movies); app.print_Movies(app.listMovies()); }
     */
}