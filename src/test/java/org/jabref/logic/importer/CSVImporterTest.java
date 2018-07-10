package org.jabref.logic.importer;

import org.jabref.logic.importer.fileformat.CSVImporter;
import org.jabref.logic.util.FileType;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CSVImporterTest {

    @Test
    void getName () {
        CSVImporter csvi = new CSVImporter ();
        assertEquals("CSV", csvi.getName ());
    }

    @Test
    void getFileType () {
        CSVImporter csvi = new CSVImporter ();
        assertEquals(FileType.CSV, csvi.getFileType ());
    }

    @Test
    void getId () {
        CSVImporter csvi = new CSVImporter ();
        assertEquals("csv", csvi.getId ());
    }

    @Test
    void getDescription () {
        CSVImporter csvi = new CSVImporter ();
        assertEquals("Importer for CSV format.", csvi.getDescription ());
    }

    @Test
    void isRecognizedFormatTrue () throws IOException {
        CSVImporter csvi = new CSVImporter ();
        BufferedReader br = new BufferedReader(new FileReader("/docs/home-hdd/jbarbirato-hdd/" +
                "Documents/UFSCar/ES2/JabRef-ES2/teste.csv"));
        assertTrue(csvi.isRecognizedFormat ( br ));
    }

    @Test
    void isRecognizedFormatFalse () throws IOException {
        CSVImporter csvi = new CSVImporter ();
        BufferedReader br = new BufferedReader(new FileReader("/docs/home-hdd/jbarbirato-hdd/" +
                "Documents/UFSCar/ES2/JabRef-ES2/teste.julia"));
        assertFalse(csvi.isRecognizedFormat ( br ));
    }

    @Test
    void validImportDatabase () throws IOException {
        CSVImporter csvi = new CSVImporter ();
        ParserResult pr = csvi.importDatabase(new BufferedReader(new FileReader("/docs/home-hdd/jbarbirato-hdd/" +
                "Documents/UFSCar/ES2/JabRef-ES2/teste.csv")));
        assertFalse(pr.isInvalid ());
    }
}