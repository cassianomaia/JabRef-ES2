package org.jabref.logic.importer.fileformat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.util.FileType;
import org.jabref.model.entry.BibEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PdfContentImporterTest {

    private PdfContentImporter importer;

    @BeforeEach
    public void setUp() {
        importer = new PdfContentImporter(mock(ImportFormatPreferences.class));
    }

    @Test
    public void testsGetExtensions() {
        assertEquals(FileType.PDF_CONTENT, importer.getFileType());
    }

    @Test
    public void testGetDescription() {
        assertEquals(
                "PdfContentImporter parses data of the first page of the PDF and creates a BibTeX entry. Currently, Springer and IEEE formats are supported.",
                importer.getDescription());
    }

    @Test
    public void doesNotHandleEncryptedPdfs() throws URISyntaxException {
        Path file = Paths.get(PdfContentImporter.class.getResource("/pdfs/encrypted.pdf").toURI());
        List<BibEntry> result = importer.importDatabase(file, StandardCharsets.UTF_8).getDatabase().getEntries();
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testStringImportDatabase() throws IOException {
        assertThrows(UnsupportedOperationException.class,()->{
            importer.importDatabase("Julia");
        });
    }

    @Test
    public void testStringNullImportDatabase() throws IOException {
        assertThrows(NullPointerException.class,()->{
            importer.importDatabase((String) null);
        });
    }

    @Test
    public void testBRImportDatabase() {
        assertThrows(UnsupportedOperationException.class,()->{
            importer.importDatabase(new BufferedReader(new FileReader(".gitignore")));
        });
    }

    @Test
    public void testBRNullImportDatabase() throws IOException {
        assertThrows(NullPointerException.class,()->{
            importer.importDatabase(new BufferedReader(new FileReader((File) null)));
        });
    }

    @Test
    public void testBR2ImportDatabase() throws IOException {
        assertFalse(importer.importDatabase(Paths.get(".gitignore"), null).isEmpty());
    }

    @Test
    public void testBR2InvalidImportDatabase() throws IOException {
        assertTrue(importer.importDatabase(Paths.get("julia/.gitignore"), null).isInvalid());
    }

}
