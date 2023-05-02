package io.jenkins.plugins.report.jtreg.main.comparator;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JobsByQueryTest {
    // WARNING! be really careful when changing this path, at the end of the test, the contents of this directory will be deleted!
    private static final String pathToJobsDir = "src/test/resources/io/jenkins/plugins/report/jtreg/main/comparator/dummyJobs/";

    @BeforeClass
    public static void createDummyJobDirs() {
        new File(pathToJobsDir + "crypto~tests-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.fips.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp17-ojdk17~rpms-f36.x86_64-release.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~full-jp17-ojdk17~rpms-f36.x86_64-release.sdk-f36.x86_64.vagrant-wayland.defaultgc.ignorecp.lnxagent.jfron").mkdirs();
        new File(pathToJobsDir + "jtreg~tier1-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "jtreg~tier1-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "reproducers~regular-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.defaultcp.lnxagent.jfroff").mkdirs();
        new File(pathToJobsDir + "rhqe-jp11-ojdk11~rpms-f36.x86_64-slowdebug.sdk").mkdirs();
    }

    private static ArrayList<String> convertJobsListToNamesList(ArrayList<File> jobsList) {
        ArrayList<String> namesList = new ArrayList<>();
        for (File job : jobsList) {
            namesList.add(job.getName());
        }
        return namesList;
    }

    @Test
    public void testFullCorrectQueryWithJob() {
        String queryString = "jtreg~full jp17 ojdk17~rpms f36 x86_64 fastdebug sdk f36 x86_64 testfarm x11 shenandoah ignorecp lnxagent jfroff";
        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(1, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testFullWrongQueryWithJob() {
        String queryString = "jtreg~tier1 jp17 ojdk17~rpms f36 x86_64 fastdebug sdk f36 x86_64 testfarm x11 shenandoah ignorecp lnxagent jfroff";
        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertFalse(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testQueryWithAsterisks() {
        String queryString = "jtreg~full * * * * * * * * * * defaultgc * * jfroff";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(3, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-release.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testQueryWithSets() {
        String queryString = "jtreg~full jp17 ojdk17~rpms f36 x86_64 {fastdebug,release} sdk f36 x86_64 {testfarm,vagrant} {x11,wayland} defaultgc ignorecp lnxagent {jfroff,jfron}";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(3, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-release.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-release.sdk-f36.x86_64.vagrant-wayland.defaultgc.ignorecp.lnxagent.jfron"));
    }

    @Test
    public void testQueryWithUnclosedSet() {
        // should throw en exception
        String queryString = "jtreg~full jp17 ojdk17~rpms f36 x86_64 fastdebug sdk f36 x86_64 testfarm x11 {defaultgc,shenandoah ignorecp lnxagent jfroff";
        try {
            JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
            fail("The test did not threw an exception.");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testQueryWithExclamationMarks() {
        String queryString = "jtreg~full !jp8 !ojdk8~rpms f36 x86_64 fastdebug sdk f36 x86_64 testfarm x11 !shenandoah ignorecp lnxagent jfroff";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(2, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testQueryWithExclamationMarksWithSets() {
        String queryString = "!{reproducers~regular,crypto~tests} jp17 ojdk17~rpms f36 x86_64 fastdebug sdk f36 x86_64 testfarm x11 defaultgc !{defaultcp,fips} lnxagent jfroff";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(2, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~tier1-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testShorterQueryWithLongerJob() {
        String queryString = "jtreg~full jp17 ojdk17~rpms f36 x86_64 fastdebug sdk";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(2, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.defaultgc.ignorecp.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("jtreg~full-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.testfarm-x11.shenandoah.ignorecp.lnxagent.jfroff"));
    }

    @Test
    public void testQueryWithShorterJob() {
        String queryString = "rhqe jp11 ojdk11~rpms f36 x86_64 slowdebug sdk f36 x86_64 vagrant x11 defaultgc legacy lnxagent jfroff";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());

        Assert.assertEquals(1, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("rhqe-jp11-ojdk11~rpms-f36.x86_64-slowdebug.sdk"));
    }

    @Test
    public void testQueryWithCombinationOfAll() {
        String queryString = "!{jtreg~full,jtreg~tier1} {jp11,jp17} {ojdk11~rpms,ojdk17~rpms} f36 x86_64 fastdebug sdk f36 x86_64 vagrant x11 !shenandoah * *";

        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        ArrayList<String> containsJobs = convertJobsListToNamesList(jbq.getJobs());
        System.out.println(containsJobs);

        Assert.assertEquals(2, containsJobs.size());
        Assert.assertTrue(containsJobs.contains("crypto~tests-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.fips.lnxagent.jfroff"));
        Assert.assertTrue(containsJobs.contains("reproducers~regular-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.defaultcp.lnxagent.jfroff"));
    }

    @Test
    public void testPrintJobs() {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final PrintStream originalStream = System.out;
        System.setOut(new PrintStream(outStream));

        String queryString = "!{jtreg~full,jtreg~tier1} {jp11,jp17} {ojdk11~rpms,ojdk17~rpms} f36 x86_64 fastdebug sdk f36 x86_64 vagrant x11 !shenandoah * *";
        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        jbq.printJobs(false, "", 0);

        Assert.assertEquals("crypto~tests-jp11-ojdk11~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.fips.lnxagent.jfroff\n" +
                "reproducers~regular-jp17-ojdk17~rpms-f36.x86_64-fastdebug.sdk-f36.x86_64.vagrant-x11.defaultgc.defaultcp.lnxagent.jfroff\n", outStream.toString());

        System.setOut(originalStream);
    }

    @Test
    public void testPrintVariants() {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final PrintStream originalStream = System.out;
        System.setOut(new PrintStream(outStream));

        String queryString = "!{crypto~tests,reproducers~regular} * * f36 * !slowdebug sdk f36 x86_64 {testfarm,vagrant} * * * * *";
        JobsByQuery jbq = new JobsByQuery(queryString, pathToJobsDir, -1);
        jbq.printVariants();

        Assert.assertEquals("1) jtreg~full, jtreg~tier1, \n" +
                "2) jp11, jp17, \n" +
                "3) ojdk11~rpms, ojdk17~rpms, \n" +
                "4) f36, \n" +
                "5) x86_64, \n" +
                "6) fastdebug, release, \n" +
                "7) sdk, \n" +
                "8) f36, \n" +
                "9) x86_64, \n" +
                "10) testfarm, vagrant, \n" +
                "11) x11, wayland, \n" +
                "12) defaultgc, shenandoah, \n" +
                "13) ignorecp, \n" +
                "14) lnxagent, \n" +
                "15) jfroff, jfron, \n", outStream.toString());

        System.setOut(originalStream);
    }

    @AfterClass
    public static void deleteDummyJobs() throws IOException {
        File[] dummyJobs = new File(pathToJobsDir).listFiles();
        if (dummyJobs != null) {
            for (File job : dummyJobs) {
                job.delete();
            }
        }
        new File(pathToJobsDir + ".placeholder").createNewFile();
    }
}