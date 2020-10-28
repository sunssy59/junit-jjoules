package org.powerapi.jjoules;

import org.junit.After;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * 28/10/2020
 */
public abstract class AbstractTest {

    private static final String FOLDER_PATH = "target/jjoules-reports/";

    protected List<String> pathToAssert = new ArrayList<>();

    @After
    public void tearsDown() {
        for (String path : this.pathToAssert) {
            final File reportFile = new File(FOLDER_PATH, path);
            assertTrue(reportFile.exists());
        }
    }

}
