import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Output {

    public static void guardar(
            String archivo, String contenido)
            throws IOException {

        Files.writeString(
                Path.of(archivo),
                contenido,
                StandardCharsets.UTF_8
        );
    }
}
