import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FindLocation {

    public static void main(String[] args) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pstr = null;
        ResultSet rst = null;
        ResultSet rs = null;
        Statement st = null;
        Statement del = null;

        String url = "jdbc:mysql://mysql11.cp.hostnet.nl:3306/db30984_stayc";
        String user = "u30984_fhd";
        String password = "stayconnected";
        
        List<String> cits = new ArrayList<String>();
        float lasttime;
        int counts = 0;
        
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT * FROM sortedDatabase");
            rs = pst.executeQuery();

            while (rs.next()) {
            	cits.clear();
            	String accountName = rs.getString(1);
            	float time = Float.valueOf(rs.getString(2));
                float latitude = Float.valueOf(rs.getString(3));
                float longitude = Float.valueOf(rs.getString(4));
                String sexs = rs.getString(6);
                
                //System.out.print("Account Name: " + accountName);
                //System.out.print(" -- Latitude: " + latitude);
                //System.out.print(" -- Longitude: " + longitude);
                //System.out.println(" -- Sex: " + sexs);
                
                pstr = con.prepareStatement("SELECT * FROM profiles WHERE accountName = '" + accountName + "'");
                rst = pstr.executeQuery();
                
	            if (rst.next()) {
	            	lasttime = time;
	            	//System.out.println(time);
	            	
                	//String sex = rst.getString(2);
                	//float activity = Float.valueOf(rst.getString(3));
                	String cities = rst.getString(4);
                	
                	//System.out.print("Account Name: " + accountName);
                    //System.out.print(" -- Sex: " + sex);
                    //System.out.print(" -- Activity: " + activity);
                    //System.out.println(" -- Cities: " + cities);
                    
                    URL link;
                	
                	link = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=false");
                	HttpURLConnection connection = (HttpURLConnection)link.openConnection();
                	connection.setRequestMethod("GET");
                	connection.connect();
                	
                	InputStream in = connection.getInputStream();
                	
                	String inputStreamString = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
                	
                	String cit;
                	
                	cities = cities.substring(1, cities.length()-1);
                	String[] citss = cities.split(",");
                	for(String loc: citss) {
                		if(!cits.contains(loc)) {
                			//if(time != 0.0) {
                			//	loc = loc + "(" + time + ")";
                			//}
                			cits.add(loc.trim());
                		}
                	}
                	
                	//System.out.println("Old cities: " + cits);
                	
                	String[] locations = inputStreamString.split("\n");
                	int index = 1;
                	for(String loc: locations){
                		if(loc.contains("\"locality\", \"political\"")){
                			int lastindex = index - 3;
                			String[] city = locations[lastindex].split(":");
                			for(String locs: city) {
                				if(locs.contains("\",")) {
                					cit = locs.substring(2, locs.length()-2);
                					if(!cits.contains(cit)) {
                						System.out.println("new city found: " + cit);
                						//if(time != 0.0) {
                            			//	cit = cit + "(" + time + ")";
                            			//	System.out.println(cit);
                            			//}
                						cits.add(cit.trim());
                					}
                				}
                			}
                		}
                		index++;
                	}
                	
                	//if(cits.toString().startsWith("[, ")) {
                	//	cits.remove(0);
                	//}

					//System.out.println(cits);
					
					con.setAutoCommit(false);
                    st = con.createStatement();
                	st.addBatch("UPDATE profiles set cities = '" + cits.toString() + "' WHERE accountName = '" + accountName + "'");
                	st.executeBatch();
                	counts++;
                	del = con.createStatement();
                	
                	//System.out.println(counts.length + " updates done!");
                }
	            else {
                	String sex = sexs;
                	float activity = 50.0f;
                	URL link;
                	
                	link = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=false");
                	HttpURLConnection connection = (HttpURLConnection)link.openConnection();
                	connection.setRequestMethod("GET");
                	connection.connect();
                	
                	InputStream in = connection.getInputStream();
                	
                	String inputStreamString = new Scanner(in,"UTF-8").useDelimiter("\\A").next();
                	
                	String cit;
                	
                	String[] locations = inputStreamString.split("\n");
                	int index = 1;
                	for(String loc: locations){
                		if(loc.contains("\"locality\", \"political\"")){
                			int lastindex = index - 3;
                			String[] city = locations[lastindex].split(":");
                			for(String locs: city) {
                				if(locs.contains("\",")) {
                					cit = locs.substring(2, locs.length()-2);
                					if(!cits.contains(cit)) {
                						cits.add(cit.trim());
                					}
                				}
                			}
                		}
                		index++;
                	}
                	
                	System.out.println("Creating new profile with account name: " + accountName);
                	
                	con.setAutoCommit(false);
                    st = con.createStatement();
                	st.addBatch("INSERT INTO profiles(accountName, sex, activity, cities) "+
                			"VALUES('"+accountName+"', '"+sex+"', '"+activity+"', '"+cits.toString()+"')");
                	st.executeBatch();
                	counts++;
                	del = con.createStatement();
                }
            }

            con.commit();
            System.out.println(counts + " updates done!");
            
        } catch (SQLException ex) {
        	Logger lgr = Logger.getLogger(MySQLConnection.class.getName());
        	lgr.log(Level.SEVERE, ex.getMessage(), ex);
    	} 
        
        
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (st != null) {
                    st.close();
                }
                if (del != null) {
                    del.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(FindLocation.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
