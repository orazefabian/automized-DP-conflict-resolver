import dp.api.maven.CentralMavenAPI;
import dp.resolver.parse.FactBuilder;
import dp.resolver.asp.AnswerSetData;
import dp.resolver.tree.generator.TreeGeneratorImpl;
import dp.resolver.tree.generator.ConflictType;
import dp.resolver.tree.generator.TreeGenerator;
import dp.resolver.tree.element.CallNode;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class TestOnSampleProject {

    private static TreeGenerator tree;
    private static String testProjectPath;
    private static AnswerSetData answer;

    private static List<String> answerOne;
    private static List<String> answerTwo;
    private static List<List<String>> expectedAnswer;

    @BeforeAll
    public static void setup() {
        initTargetPath();

        prepareExpectedOutcomes();
        prepareNeededJarsFromResources();

        answer = new AnswerSetData();
        CentralMavenAPI.setMaxVersionsNumFromCmr(5);
        tree = new TreeGeneratorImpl(testProjectPath, answer);

        start();
    }

    private static void prepareNeededJarsFromResources() {
        ProcessBuilder pb = new ProcessBuilder();

        // run mvn install command for every needed jar in the resource folder, note that the order is important!
        try {
            mvnInstallJar("Project_D/3.0/");
            mvnInstallJar("Project_D/2.0/");
            mvnInstallJar("Project_D/1.0/");

            mvnInstallJar("Project_C/1.0/");
            mvnInstallJar("Project_C/2.0/");

            mvnInstallJar("Project_B/1.0/");
            mvnInstallJar("Project_B/2.0/");

        }catch (IOException | InterruptedException e){
            e.printStackTrace();
            System.err.println("Failed setup, not able to complete installation of jars to local .m2 repo");
        }
    }

    private static void mvnInstallJar(String jarPath) throws IOException, InterruptedException {
        ProcessBuilder pb;
        String cmd = "mvn compile install";
        String cleanUpCmd = "mvn clean";
        if (System.getProperty("os.name").startsWith("Windows")) {
            pb = new ProcessBuilder("cmd.exe", "/c", "cd " + getCurrDirectory() + "/src/test/resources/" + jarPath + " && " + cmd + " && " + cleanUpCmd);
        } else {
            pb = new ProcessBuilder("/bin/bash", "-c", "cd " + getCurrDirectory() + "/src/test/resources/" + jarPath + " ; " + cmd + " ; " + cleanUpCmd);
        }
        Process process = pb.start();
        process.waitFor();
    }

    private static void prepareExpectedOutcomes() {
        answerOne = new ArrayList<>();
        answerOne.add(getOSPrefixForM2Repo() + "/.m2/repository/org/runtime/conflict/Project_B/2.0/Project_B-2.0.jar");
        answerOne.add(getOSPrefixForM2Repo() + "/.m2/repository/org/runtime/conflict/Project_C/2.0/Project_C-2.0.jar");

        answerTwo = new ArrayList<>();
        answerTwo.add(getOSPrefixForM2Repo() + "/.m2/repository/org/runtime/conflict/Project_B/2.0/Project_B-2.0.jar");
        answerTwo.add(getOSPrefixForM2Repo() + "/.m2/repository/org/runtime/conflict/Project_C/1.0/Project_C-1.0.jar");

        expectedAnswer = new ArrayList<>();
        expectedAnswer.add(answerOne);
        expectedAnswer.add(answerTwo);

        Collections.sort(answerOne);
        Collections.sort(answerTwo);
    }

    private static void initTargetPath() {
        testProjectPath = getCurrDirectory() + "/src/test/resources/Project_A/";
    }

    private static String getCurrDirectory() {
        return System.getProperty("user.dir");
    }

    private static boolean isLinuxOS() {
        return System.getProperty("os.name").startsWith("Linux");
    }

    private static String getOSPrefixForM2Repo() {
        if (isLinuxOS()) {
            return "/home/" + System.getProperty("user.name");
        } else {
            return "/Users/" + System.getProperty("user.name");
        }
    }

    private static void start() {
        try {
            tree.computeCallTree();
            FactBuilder parser;
            parser = new FactBuilder(tree.getConflicts(ConflictType.TYPE_3), tree.getNeededJars());
            answer.setIDMap(parser.getIdMap());
            answer.solve();
        } catch (Exception e) {
        }
        sortAnswer(answer);
    }

    private static void sortAnswer(AnswerSetData answer) {
        for (List<String> list : answer.getAnswers()) {
            Collections.sort(list);
        }
    }

    @AfterAll
    public static void tearDown() {
        answer = null;
        tree = null;
        answerOne = null;
        answerTwo = null;
        expectedAnswer = null;
    }

    @Test
    public void testCorrectAmountBloatedJars() {
        Assertions.assertEquals(2, answer.getBloatedJars().size());
    }

    @Test
    public void testCorrectAmountAnswers() {
        Assertions.assertEquals(2, answer.getAnswers().size());
    }

    @Test
    public void testCorrectAnswerArrays() {
        Assertions.assertArrayEquals(expectedAnswer.toArray(), answer.getAnswers().toArray());
    }



    @Test
    public void testCorrectConflictNodes() {
        List<CallNode> conflicts = tree.getConflicts(ConflictType.TYPE_3);
        testNodeAt0(conflicts);
        Assertions.assertEquals(8, conflicts.size());
    }

    private void testNodeAt0(List<CallNode> conflicts) {
        String extraObjectD = "conflict.Object_D";
        String suffix = "3.0.jar";
        boolean found = false;

        for (CallNode node : conflicts) {
            if (node.getClassName().equals(extraObjectD) && node.getFromJar().endsWith(suffix)) {
                found = true;
            }
        }
        Assertions.assertTrue(found);
    }

    private void testNodeAt1(List<CallNode> conflicts) {
        String objectD = "conflict.Object_D";
        String suffix = "2.0.jar";
        boolean found = false;
        for (CallNode node : conflicts) {
            if (node.getClassName().equals(objectD) && node.getFromJar().endsWith(suffix)) {
                found = true;
            }
        }
        Assertions.assertTrue(found);
    }


}
