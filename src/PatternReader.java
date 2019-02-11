import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

class PatternReader {

    ArrayList<int[]> parseFileArray(ArrayList<String> lines) {
        // take read file, and grab cell coordinates
        ArrayList<int[]> coords = new ArrayList<int[]>();
        for (String line : lines) {
            if (!line.contains("#")) {
                String[] strPair = line.split(" ");
                int[] intPair = new int[2];
                intPair[0] = Integer.parseInt(strPair[0]);
                intPair[1] = Integer.parseInt(strPair[1]);
                coords.add(intPair);
            }
        }
        return coords;
    }

    ArrayList<String> readFile(String filename) {
        // read file into ArrayList
        filename = String.format("Patterns/%s.lif", filename);

        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<String> readLoadedFile(File file) {
        // similar to readFile, but doesn't require file to be in Patterns
        ArrayList<String> lines = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            return lines;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", file);
            e.printStackTrace();
            return null;
        }
    }
}
