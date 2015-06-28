import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.CPSInsertRequest;
import com.clusterpoint.api.request.CPSRetrieveRequest;
import com.clusterpoint.api.request.CPSUpdateRequest;
import com.clusterpoint.api.response.CPSListLastRetrieveFirstResponse;
import com.clusterpoint.api.response.CPSModifyResponse;
import com.google.gson.Gson;

public class DatabaseWrapper
{
	public static void main(String[] args)
	{
		
	}
	
	private class ThingsList
	{
		public ThingsList(ArrayList<Thing> list)
		{
			this.things = list;
		}

		private ArrayList<Thing> things;

		public ArrayList<Thing> getThings()
		{
			return things;
		}
	}

	public synchronized void saveToDB(boolean lost, ArrayList<Thing> things)
	{
		try
		{
			String id = lost ? "lost" : "found";
			CPSConnection conn = new CPSConnection("tcps://cloud-us-0.clusterpoint.com:9008", "lostandfound", "simoncqm@gmail.com", "cqxmzz", "100639", "document", "//document/id");
			Gson gson = new Gson();
			String json_data = gson.toJson(new ThingsList(things));
			String doc = "<document><id>" + id + "</id><data>" + json_data + "</data></document>";
			CPSUpdateRequest update_req = new CPSUpdateRequest(doc);
			CPSModifyResponse update_resp = (CPSModifyResponse) conn.sendRequest(update_req);
			System.out.println("Updated ids: " + Arrays.toString(update_resp.getModifiedIds()));
			// Close connection
			conn.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public synchronized ArrayList<Thing> getFromDB(boolean lost)
	{
		try
		{
			String id = lost ? "lost" : "found";
			CPSConnection conn = new CPSConnection("tcps://cloud-us-0.clusterpoint.com:9008", "lostandfound", "simoncqm@gmail.com", "cqxmzz", "100639", "document", "//document/id");
			// Retrieve single document specified by document id
			CPSRetrieveRequest retr_req = new CPSRetrieveRequest(id);
			CPSListLastRetrieveFirstResponse retr_resp = (CPSListLastRetrieveFirstResponse) conn.sendRequest(retr_req);
			List<Element> docs = retr_resp.getDocuments();
			Iterator<Element> it = docs.iterator();
			
			String data = "";
			
			while (it.hasNext())
			{
				Element el = it.next();
				NodeList nodes = el.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++)
				{
					if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
					{
						Element e = (Element) nodes.item(i);
						if (e.getTagName().equals("data"))
						{
							data = e.getTextContent();
							break;
						}
					}
				}
				// here goes the code extracting data from DOM Element
			}
			Gson gson = new Gson();
			ThingsList tl = gson.fromJson(data, ThingsList.class);
			return tl.getThings();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
