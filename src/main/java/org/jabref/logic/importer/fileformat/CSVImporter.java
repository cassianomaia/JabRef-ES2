package org.jabref.logic.importer.fileformat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jabref.logic.importer.Importer;
import org.jabref.logic.importer.ParserResult;
import org.jabref.logic.util.FileType;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

/**
 * Importer for CSV format.
 *
 * Documentation can be found online at:
 *
 * http://csv.ac.uk/faq/#format
 */

public class CSVImporter extends Importer {

    private static final Pattern CSV_PATTERN = Pattern.compile("\"([^\"]+?)\",?|([^,]+),?|,");

    @Override
    public String getName() {
        return "CSV";
    }

    @Override
    public FileType getFileType() {
        return FileType.CSV;
    }

    @Override
    public String getId() {
        return "csv";
    }

    @Override
    public String getDescription() {
        return "Importer for CSV format.";
    }

    @Override
    public boolean isRecognizedFormat(BufferedReader reader) throws IOException {
        String str;
        try{
            while ((str = reader.readLine()) != null) {
                if (CSVImporter.CSV_PATTERN.matcher(str).find()) {
                    return true;
                }
            }
        }catch (FileNotFoundException e){
            System.out.println ( "No such file or directory" );
        }
        return false;
    }

    @Override
    public ParserResult importDatabase(BufferedReader reader) throws IOException{
        List<BibEntry> results = new ArrayList<>();

        // Preprocess entries
        String str;
        try {
            while ((str = reader.readLine()) != null) {

                String [] line = str.split(",");
                BibEntry b = new BibEntry();

                if(line[0].equals("Book")){
                    b.setType("book");
                }else if (line[0].equals("Article")) {
                    b.setType("article");
                }
                setOrAppend(b, FieldName.TITLE, line[1], ", ");
                setOrAppend(b, FieldName.AUTHOR, line[2], " and ");
                setOrAppend(b, FieldName.YEAR, line[3], ", ");
                setOrAppend(b, FieldName.PUBLISHER, line[4], ", ");
                setOrAppend(b, FieldName.JOURNAL, line[5], ", ");
                results.add(b);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return new ParserResult(results);

    }
    private static void setOrAppend(BibEntry b, String field, String value, String separator) {
        if (b.hasField(field)) {
            b.setField(field, b.getField(field).get() + separator + value);
        } else {
            b.setField(field, value);
        }
    }
}