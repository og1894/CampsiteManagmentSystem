import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class XStreamPersistence {

    private static final String FILE_NAME = "campsite-data.xml";

    private XStream createXStream() {
        XStream xstream = new XStream(new DomDriver());
        xstream.alias("systemData", SystemData.class);
        xstream.alias("campsite", Campsite.class);
        xstream.alias("campingArea", CampingArea.class);
        xstream.alias("plot", Plot.class);
        xstream.alias("guest", Guest.class);
        xstream.alias("booking", Booking.class);
        xstream.alias("linkedList", LinkedList.class);

        xstream.allowTypesByWildcard(new String[]{"campsite.**"});
        return xstream;
    }

    public void save(SystemData data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("System data cannot be null.");
        }

        XStream xstream = createXStream();

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            xstream.toXML(data, writer);
        }
    }

    public SystemData load() throws IOException {
        XStream xstream = createXStream();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            Object loadedObject = xstream.fromXML(reader);
            return (SystemData) loadedObject;
        }
    }
}