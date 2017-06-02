package utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import testData.MailData;

public class HTMLGenerator {
	public String generateHTMLFromTemplate(List<MailData> mailDataList, String time) throws IOException, TemplateException{
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("time", time);
		map.put("mailData", mailDataList);

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		
		cfg.setDirectoryForTemplateLoading(new File("src/main/resources/template"));
		
		cfg.setDefaultEncoding("UTF-8");

		// Sets how errors will appear.
		// During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Don't log exceptions inside FreeMarker that it will thrown at you anyway:
		cfg.setLogTemplateExceptions(false);

		// Where do we load the templates from:
		Template template = cfg.getTemplate("alertMailer.ftl");

		StringWriter stringWriter = new StringWriter();
		template.process(map, stringWriter);

		// get the String from the StringWriter
		String string = stringWriter.toString();
		
		return string;
	}


}
