package org.prado.ide.contentassist.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.prado.ide.contentassist.beans.Component;

/**
 * This loader loads component from Prado website and caches it at local documentation path
 * @author anand
 *
 */
public class RemoteComponentLoader extends LocalComponentLoader{
	
	private String url;
	
	public RemoteComponentLoader(String url, String localCacheFolder) {
		super(localCacheFolder);
		this.setUrl(url);
	}

	@Override
	public Component loadComponent(String componentName) {
		try {
			URL url = new URL(this.url + "/" + componentName );
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
						
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			new File(getLocalCacheFolder()).mkdir();
			
			File file = new File(getLocalCacheFolder() + File.separator + componentName);						
			PrintWriter pw = new PrintWriter(new FileOutputStream(file));
			
			String str = "";
			
			while((str = br.readLine())!=null) {
				pw.println(str);
			}			
						
			pw.close();
			br.close();
			
			return super.loadComponent(componentName);
			
		} catch (Throwable e) {
						
			e.printStackTrace();
		}
		return null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
