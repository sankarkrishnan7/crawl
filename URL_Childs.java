import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import org.json.simple.JSONObject;

class linkHolders {
	String link;
	HashSet<String> links = new HashSet<String>();
	HashMap<String, linkHolders> child = new HashMap<String, linkHolders>();

	public String toString() {

		JSONObject obj = new JSONObject();
		if (child.isEmpty()) {
			obj.put("Parent", link);
			obj.put("Child(s)", links.toString());
			return (obj.toJSONString());
		} else {
			obj.put("Parent", link);
			obj.put("Child(s)", child.values());
			return (obj.toJSONString());
		}
	}
}

public class URL_Childs {

	public static void main(String[] args) {

		String li = "http://php.net/manual/en/tutorial.firstpage.php";
		int depth = 1;
		URL_Childs urlpack = new URL_Childs();
		linkHolders lnk = urlpack.extractFromURL(li);
		lnk = urlpack.update_listholder(lnk, depth);
		System.out.println(lnk);
	}

	public linkHolders extractFromURL(String url_) {

		linkHolders major = new linkHolders();
		try {
			URL url = new URL(url_);

			major.link = url.toString();
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("href=")) {

					major.links
							.add(line.trim().split("href=")[1]
									.startsWith("\"http") ? line.trim().split(
									"href=")[1].split("\"")[1] : null);
				}
				major.links.remove(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return major;
	}

	public linkHolders update_listholder(linkHolders linkholder, int depth) {

		if (depth > 0) {
			for (String url : linkholder.links) {

				linkholder.child.put(url,
						update_listholder(extractFromURL(url), depth - 1));

			}
		}
		return linkholder;
	}

}