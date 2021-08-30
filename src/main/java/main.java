import java.io.File;
import java.io.IOException;

public class main {

    public static void main(String[] args) {
//        ImageComparator util = new ImageComparator();
//        ClassLoader classLoader = main.class.getClassLoader();
//
//        System.out.println(util.visuallyCompare(new File(classLoader.getResource("poemLogo.jpeg").getFile()),
//                new File("/Users/sunnysrivastava/IdeaProjects/loconotion/dist/welcome-to-my-piece-of-internet/assets/images/eeb1d1cfae042ac471a9e333b141b6970c88ecee.jpeg")));
        RedundantImageCleaner cleaner = new RedundantImageCleaner();
        try {
            cleaner.cleanReduntantImagesfromAssetFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
