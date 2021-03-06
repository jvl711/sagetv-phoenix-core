package test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import phoenix.impl.CommercialAPI;
import phoenix.impl.CommercialStruct;
import test.InitPhoenix;

public class TestEDLCommercials {
    @BeforeClass
    public static void init() throws IOException {
        InitPhoenix.init(true, true);
    }

    @Test
    public void testCommercialFiles() {
        File f = InitPhoenix.ProjectHome("src/test/java/test/junit/EDLTestFile.avi");
        CommercialAPI api = new CommercialAPI();
        File edlFile = api.GetCommercialFile(f);
        System.out.println("EDL: " + edlFile);
        assertTrue("No EDL File!", edlFile.exists());
        assertTrue("Failed EDL File test", api.HasCommercials(f));

        CommercialStruct commercials[] = api.getCommercials(f);
        assertNotNull("No Commercials", commercials);

        for (CommercialStruct o : commercials) {
            System.out.printf("Commercial: %s; %s\n", phoenix.comskip.GetCommercialStart(o), phoenix.comskip.GetCommercialStop(o));
        }

        assertEquals(83.78, commercials[0].start, 0.5);
        assertEquals(299.27, commercials[0].stop, 0.5);
    }
}
