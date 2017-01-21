package test.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File("c:/temp/tmp/Serviio/pics.txt")));
		String line;
		int count = 1;
		HttpClient http = HttpClients.custom().build();
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
					HttpGet method = new HttpGet(line);
					try {
						CloseableHttpResponse response = (CloseableHttpResponse) http.execute(method);
						
						filename = line.substring(line.lastIndexOf("."));
						File pof = new File("c:/temp/tmp/Serviio/pof/" + count + filename);
						
						HttpEntity entity = response.getEntity();
						
						if (entity != null) {
							FileUtils.writeByteArrayToFile(pof, EntityUtils.toByteArray(entity));	
						}
						
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
