package org.jabref.logic.importer.fileformat;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class GvkParserTest {

    private GvkParser parser;

    @BeforeEach
    void setUp(){
        parser = mock(GvkParser.class);
    }

    @Test
    public void testParseEntries(){
        //TODO: Não é possível testar esse método por conta das chamadas privadas.
    };

}
