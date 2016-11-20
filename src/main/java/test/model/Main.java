package test.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File("c:/temp/tmp/Serviio/pics.txt")));
		String line;
		int count = 1;
		HttpClient http = new HttpClient();
		String filename;
        // put in comment
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith("http://pic")
//						&& line.contains(".pof.com/thumbnails/")
						&& line.contains(".pof.com/dating/")
						&& line.endsWith(".jpg")) {
					count++;
					System.out.println(count + " = " + line);
					HttpMethod method = new GetMethod(line);
					try {
						http.executeMethod(method);
						
						filename = line.substring(line.lastIndexOf("."));
						File pof = new File("c:/temp/tmp/Serviio/pof/" + count + filename);
						FileUtils.writeByteArrayToFile(pof, method.getResponseBody());
					} finally {
						method.releaseConnection();
					}
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			IOUtils.closeQuietly(br);
		}
	}
}
