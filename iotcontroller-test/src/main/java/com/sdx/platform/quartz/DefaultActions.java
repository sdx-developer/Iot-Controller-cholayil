package com.sdx.platform.quartz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.groovy.control.CompilationFailedException;

import com.sdx.platform.config.Constants;
import com.sdx.platform.config.Memory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultActions {

	@SuppressWarnings("resource")
	public static void init() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final GroovyClassLoader classLoader = new GroovyClassLoader();
		
	    URL url = loader.getResource(Constants.SDX_GROOVY_EXTENSIONS_PATH);
	    String path = url.getPath();
	    
	    List<File> groovies = Arrays.asList(new File(path).listFiles());
	    groovies.stream().forEach(new Consumer<File>() {
			public void accept(File gFile) {
				Class<?> groovy;
				try {
					log.info("Current process "+gFile.getName());
					groovy = classLoader.parseClass(FileUtils.readFileToString(gFile, Charset.defaultCharset()));
					GroovyObject groovyObj;
					
					try {
						groovyObj = (GroovyObject) groovy.newInstance();
						//groovyObj.invokeMethod("convertToJSON", null);
						Memory.getGroovyContent().put(FilenameUtils.getBaseName(gFile.getName()), FileUtils.readFileToString(gFile, Charset.defaultCharset()));
						Memory.getGroovyObjects().put(FilenameUtils.getBaseName(gFile.getName()), groovyObj);
						
						String json = (String) groovyObj.invokeMethod("buildJUI", null);
						
					} catch (InstantiationException | IllegalAccessException e) {
						log.error("Error while groovy extensions invocation ", e);
					}
					
				} catch (CompilationFailedException | IOException  e) {
					log.error("Error while groovy extensions registration ", e);
				}
				
			}
		});
	    
	    
	}
	
	/*try {
	String fileContent = FileUtils.readFileToString(new File(ruleURL), Charset.defaultCharset());
	String newMD5 = ChecksumCalculator.getMD5Hash(fileContent);

	final GroovyClassLoader classLoader = new GroovyClassLoader();
	Class<?> groovy = classLoader.parseClass(fileContent);
	GroovyObject groovyObj = (GroovyObject) groovy.newInstance();
	groovyObj.invokeMethod("convertToJSON", null);

	Memory.getExtensionsContent().put(ruleName, fileContent);
	Memory.getExtensionObjects().put(ruleName, groovyObj);

	log.info("New MD5 value " + newMD5 + ", Saved to Memory");

} catch (IOException | InstantiationException | IllegalAccessException e) {
	e.printStackTrace();
}*/

}
