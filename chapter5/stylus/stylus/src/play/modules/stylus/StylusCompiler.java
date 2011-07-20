package play.modules.stylus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import play.Logger;
import play.Play;

public class StylusCompiler {

	public String compile(File realFile) throws Exception {
		if (!realFile.exists() || !realFile.canRead()) {
			throw new FileNotFoundException(realFile + " not found");
		}
		String stylusPath = Play.configuration.getProperty("stylus.executable", "/usr/local/share/npm/bin/stylus");

		File stylusFile = new File(stylusPath);
		if (!stylusFile.exists() || !stylusFile.canExecute()) {
			throw new FileNotFoundException(stylusFile + " not found");
		}

		Process p = new ProcessBuilder(stylusPath).start();
		byte data[] = FileUtils.readFileToByteArray(realFile);
		p.getOutputStream().write(data);
		p.getOutputStream().close();

		InputStream is = p.getInputStream();
		String output = IOUtils.toString(is);
		is.close();

		return output;
	}

}
