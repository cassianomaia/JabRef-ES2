package org.jabref.logic.importer.fileformat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.fest.util.FilesException;
import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.logic.importer.Parser;
import org.jabref.logic.importer.ParserResult;
import org.jabref.logic.util.FileType;
import org.jabref.model.entry.BibEntry;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EndnoteXmlImporterTestFiles {

    private static final String FILE_ENDING = ".xml";
    private static final Object InputStream = new InputStream() {
        @Override
        public int read() throws IOException {
            return 0;
        }
    };
    private ImportFormatPreferences preferences;


    private static Stream<String> fileNames() throws IOException {
        Predicate<String> fileName = name -> name.startsWith("EndnoteXmlImporterTest") && name.endsWith(FILE_ENDING);
        return ImporterTestEngine.getTestFiles(fileName).stream();
    }

    private static Stream<String> invalidFileNames() throws IOException {
        Predicate<String> fileName = name -> !name.startsWith("EndnoteXmlImporterTest");
        return ImporterTestEngine.getTestFiles(fileName).stream();
    }

    @BeforeEach
    void setUp() {
        preferences = mock(ImportFormatPreferences.class);
        when(preferences.getKeywordSeparator()).thenReturn(';');
    }

    @ParameterizedTest
    @MethodSource("fileNames")
    void testIsRecognizedFormat(String fileName) throws IOException {
        ImporterTestEngine.testIsRecognizedFormat(new EndnoteXmlImporter(preferences), fileName);
    }

    @ParameterizedTest
    @MethodSource("invalidFileNames")
    public void testIsNotRecognizedFormat(String fileName) throws IOException {
        ImporterTestEngine.testIsNotRecognizedFormat(new EndnoteXmlImporter(preferences), fileName);
    }

    @ParameterizedTest
    @MethodSource("fileNames")
    void testImportEntries(String fileName) throws Exception {
        ImporterTestEngine.testImportEntries(new EndnoteXmlImporter(preferences), fileName, FILE_ENDING);
    }

    @Test
    public void testGetFileType() {
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        FileType ft = enxi.getFileType();
        assertEquals(FileType.ENDNOTE_XML, ft);
    }

    @Test
    public void testGetDescription(){
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        String str = enxi.getDescription();
        assertEquals("Importer for the EndNote XML format.", str);
    }

    @Test
    public void isRecognizedFormat() throws IOException {
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        boolean bool = enxi.isRecognizedFormat("<records>");
        assertTrue(bool);
    }


    @Test
    public void testTryElseImportDatabase() throws IOException {
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        ParserResult pr = enxi.importDatabase(new BufferedReader(new FileReader("/docs/home-hdd/jbarbirato-hdd/" +
                "Documents/UFSCar/ES2/JabRef-ES2/.gitignore")));
        assertTrue(pr.isInvalid());
    }

    @Test
    public void testTryIfImportDatabase() throws IOException {
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        ParserResult pr = enxi.importDatabase(new BufferedReader(new FileReader("/docs/home-hdd/jbarbirato-hdd/" +
                "Documents/UFSCar/ES2/JabRef-ES2/teste.xml")));
        assertTrue(pr instanceof ParserResult);
    }

    @Test
    public void testList() throws ParseException, IOException {
        EndnoteXmlImporter enxi = new EndnoteXmlImporter(preferences);
        List<BibEntry> l = enxi.parseEntries((java.io.InputStream) InputStream);
        assertEquals(enxi.importDatabase(new BufferedReader(new InputStreamReader((java.io.InputStream) InputStream,
                StandardCharsets.UTF_8))).getDatabase().getEntries(), l);
    }

}
