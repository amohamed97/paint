package fileManagement;

import java.awt.*;

public interface FileHandler {
     public void save(String fileName, Color background);
     public Color load(String fileName);
}
