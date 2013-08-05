package elevator.server.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ElevatorFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        String message = formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        return String.format("%1$TF %1$TT'%1$TL %2$16s %3$s%4$s%n",
                date,
                record.getLoggerName(),
                message,
                throwable);
    }

}
