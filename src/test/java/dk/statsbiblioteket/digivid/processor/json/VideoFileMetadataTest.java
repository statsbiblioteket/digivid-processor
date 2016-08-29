package dk.statsbiblioteket.digivid.processor.json;

import dk.statsbiblioteket.digivid.processor.VideoFileObject;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;

/**
 * Created by csr on 4/14/15.
 */
public class VideoFileMetadataTest {

    @Test
    public void testToJson() throws Exception {
        Path path = Paths.get("/a/b/foobar.ts");
        VideoFileObject videoFileObject = VideoFileObject.createFromPath(path);
        videoFileObject.setStartDate(new GregorianCalendar(1992, 01, 23, 18, 00).getTime().getTime());
        videoFileObject.setEndDate(new GregorianCalendar(1992, 01, 23, 21, 30).getTime().getTime());
        videoFileObject.setChannel("tv2");
        videoFileObject.setQuality("3");
        videoFileObject.setVhsLabel("This is the finest VHS tape I have ever seen.");
        System.out.println(videoFileObject.toJson());
    }

    @Test
    public void testFromJson() throws Exception {
        String json = "{\"filename\":\"c\"} ";
        VideoFileObject.fromJson(json);
    }
}