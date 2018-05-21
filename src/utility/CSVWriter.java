package utility;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @create 2018-05-14-21:29
 */

public class CSVWriter {
    /**
     *
     * @param w writer
     * @param data data needed to be written
     * @throws IOException
     */
    public static void writeLine(Writer w, List<String> data)
            throws IOException {
        char separators = ',';
        boolean isFirst = true;

        StringBuilder sb = new StringBuilder();
        for (String value : data) {
            // if it's not the first one
            // add separator
            if (!isFirst) {
                sb.append(separators);
            }

            sb.append(value);

            isFirst = false;
        }

        sb.append("\n");

        w.append(sb.toString());

    }

}