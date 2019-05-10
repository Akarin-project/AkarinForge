// Akarin start
package net.minecrell.terminalconsole;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * This will be overrided by resources, keeping the signatures
 */
public class TerminalConsoleAppender extends AbstractAppender {

    protected TerminalConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	@Nullable
    public synchronized static Terminal getTerminal() {
        return null;
    }

	@Override
	public void append(LogEvent event) {}
	
	public synchronized static void setReader(@Nullable LineReader newReader) {}
}
// Akarin end