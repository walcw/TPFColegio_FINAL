package ar.edu.centro8.daw.tpracticofinal.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Value("${music.external.dir:}")
    private String externalDir;

    @Value("${music.upload.max-bytes:10485760}")
    private long maxUploadBytes;

    private static final Set<String> ALLOWED_EXT = Set.of(".mp3", ".wav", ".ogg", ".m4a");

    @GetMapping("/search")
    public List<Map<String, String>> search(@RequestParam(value = "q", required = false) String q) throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        String query = (q == null) ? "" : q.toLowerCase();

        Path projectStatic = Paths.get("src", "main", "resources", "static");
        List<Path> bases = new ArrayList<>();
        if (Files.exists(projectStatic)) bases.add(projectStatic);
        if (externalDir != null && !externalDir.isBlank()) {
            Path ext = Paths.get(externalDir);
            if (Files.exists(ext)) bases.add(ext);
        }

        for (Path base : bases) {
            Files.walk(base).filter(Files::isRegularFile).forEach(p -> {
                String fileName = p.getFileName().toString();
                String lower = fileName.toLowerCase();
                boolean okExt = ALLOWED_EXT.stream().anyMatch(lower::endsWith);
                if (!okExt) return;
                if (query.isEmpty() || fileName.toLowerCase().contains(query)) {
                    Map<String, String> m = new HashMap<>();
                    m.put("title", fileName);
                    try {
                        if (base.equals(projectStatic)) {
                            Path rel = base.relativize(p);
                            String webPath = "/" + rel.toString().replace('\\', '/');
                            m.put("url", webPath);
                            m.put("type", "internal");
                        } else {
                            String encoded = URLEncoder.encode(p.toAbsolutePath().toString(), StandardCharsets.UTF_8.toString());
                            m.put("url", "/api/music/file?path=" + encoded);
                            m.put("type", "external");
                        }
                    } catch (Exception ignored) {
                    }
                    results.add(m);
                }
            });
        }

        return results;
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> file(@RequestParam("path") String path) throws IOException {
        String decoded = URLDecoder.decode(path, StandardCharsets.UTF_8);
        Path p = Paths.get(decoded);

        Path projectStatic = Paths.get("src", "main", "resources", "static").toAbsolutePath().normalize();
        boolean allowed = false;
        if (p.toAbsolutePath().normalize().startsWith(projectStatic)) allowed = true;
        if (!allowed && externalDir != null && !externalDir.isBlank()) {
            Path ext = Paths.get(externalDir).toAbsolutePath().normalize();
            if (p.toAbsolutePath().normalize().startsWith(ext)) allowed = true;
        }

        if (!allowed || !Files.exists(p) || !Files.isRegularFile(p)) return ResponseEntity.notFound().build();

        Resource resource = new FileSystemResource(p.toFile());
        String contentType = Files.probeContentType(p);
        MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok().contentType(mediaType).body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestPart("file") MultipartFile file) throws IOException {
        Map<String, String> resp = new HashMap<>();
        if (file == null || file.isEmpty()) {
            resp.put("error", "No file provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        if (file.getSize() > maxUploadBytes) {
            resp.put("error", "Archivo demasiado grande. Tamaño máximo: " + (maxUploadBytes / (1024*1024)) + " MB");
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(resp);
        }

        String filename = file.getOriginalFilename();
        if (filename == null) filename = "unnamed";
        String lower = filename.toLowerCase();
        boolean okExt = ALLOWED_EXT.stream().anyMatch(lower::endsWith);
        if (!okExt) {
            resp.put("error", "Extension no permitida");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        if (externalDir == null || externalDir.isBlank()) {
            resp.put("error", "Directorio externo no configurado");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

        Path destDir = Paths.get(externalDir);
        if (!Files.exists(destDir)) Files.createDirectories(destDir);

        Path dest = destDir.resolve(Paths.get(filename).getFileName());
        // Evitar sobrescribir: si existe, agregar sufijo
        int i = 1;
        String baseName = filename;
        String ext = "";
        int dot = filename.lastIndexOf('.');
        if (dot >= 0) {
            baseName = filename.substring(0, dot);
            ext = filename.substring(dot);
        }
        while (Files.exists(dest)) {
            String newName = baseName + "(" + i + ")" + ext;
            dest = destDir.resolve(newName);
            i++;
        }

        try {
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            resp.put("error", "No se pudo guardar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

        String encoded = URLEncoder.encode(dest.toAbsolutePath().toString(), StandardCharsets.UTF_8.toString());
        resp.put("title", dest.getFileName().toString());
        resp.put("url", "/api/music/file?path=" + encoded);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/config")
    public Map<String, Object> config() {
        Map<String, Object> m = new HashMap<>();
        m.put("maxUploadBytes", maxUploadBytes);
        m.put("maxUploadMB", maxUploadBytes / (1024 * 1024));
        return m;
    }

}
