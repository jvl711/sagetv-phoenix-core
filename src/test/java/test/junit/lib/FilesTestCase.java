package test.junit.lib;

import junit.framework.TestCase;
import sagex.phoenix.vfs.impl.FileCleaner;

import java.io.File;

public class FilesTestCase extends TestCase {
    public FilesTestCase() {
        super();
    }

    public FilesTestCase(String name) {
        super(name);
    }

    public static File makeFile(String path) {
        return TestUtil.makeFile(path, true);
    }

    public static File makeDir(String path) {
        return TestUtil.makeDir(path);
    }

    public void testFileDelete() {
        makeFile("test1/test/1.avi");
        makeFile("test1/test/2.avi");
        makeFile("test1/test2/3.avi");
        makeFile("test1/4.avi");
        File dir = makeDir("test1");
        assertEquals("exists", true, dir.exists());
        FileCleaner.clean(dir);
        assertEquals("exists after clean", false, dir.exists());
    }
}
