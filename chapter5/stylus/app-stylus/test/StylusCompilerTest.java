import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import play.Play;
import play.modules.stylus.StylusCompiler;
import play.test.UnitTest;

public class StylusCompilerTest extends UnitTest {

    @Test
    public void checkThatStylusCompilerWorks() throws Exception {
    	StylusCompiler compiler = new StylusCompiler();
    	File file = Play.getFile("test/test.styl");
    	String result = compiler.compile(file);
    	
    	File expectedResultFile = Play.getFile("test/test.styl.result");
    	String expectedResult = FileUtils.readFileToString(expectedResultFile);
    	
    	assertEquals(expectedResult, result);
    }

}
