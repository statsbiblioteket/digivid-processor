package dk.statsbiblioteket.digivid.processor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Performs initializing steps like reading information from the property file and setup the GUI
 */
public class DigividProcessor extends Application {

	protected static String recordsDir;
    protected static String channelCSV;
    protected static String player;
    protected static String localProperties;
    private static Logger log = LoggerFactory.getLogger(DigividProcessor.class);

    public static void main(String[] args) {
        String propertiesLocation = System.getProperty("digivid.config");
        if (propertiesLocation == null) {
            throw new RuntimeException("Must define location of root properties with -Ddigivid.config=....");
        }
        Path propertiesPath = Paths.get(propertiesLocation);
        if (!Files.exists(propertiesPath)) {
            throw new RuntimeException("No such file: " + propertiesPath);
        }
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(propertiesPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not read properties file " + propertiesPath, e);
        }
        recordsDir = properties.getProperty("digivid.processor.recordsdir");
        channelCSV = properties.getProperty("digivid.processor.channels");
        player = properties.getProperty("digivid.processor.player");
        localProperties = properties.getProperty("digivid.processor.localVHSProperties");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Platform.runLater(() -> TextUtils.showErrorDialog(t, e)));
        Thread.currentThread().setUncaughtExceptionHandler(TextUtils::showErrorDialog);

        primaryStage.setTitle("Video processor");
        initRootLayout(primaryStage);
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout(Stage primaryStage) {
        try {
            // Load root layout from fxml file.
            FXMLLoader rootLoader = new FXMLLoader();
            rootLoader.setLocation(getClass().getClassLoader().getResource("filelist.fxml"));
            AnchorPane rootLayout = rootLoader.load();
            final Controller controller = rootLoader.getController();
            controller.setDataPath(Paths.get(recordsDir));
            controller.loadFilenames();
            // Show the scene containing the root layout.
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene scene = new Scene(rootLayout, screenBounds.getWidth(), screenBounds.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ioe) {
            log.error("Error occured while loading file in initRootLayout", ioe);
            TextUtils.showErrorDialog(Thread.currentThread(), ioe);
        }
    }
}
