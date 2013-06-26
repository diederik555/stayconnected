import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FindMatch {

    public static void main(String[] args) throws IOException {

        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pstl = null;
        ResultSet rs = null;
        ResultSet rsl = null;
        Statement st = null;
        Statement del = null;

        String url = "jdbc:mysql://mysql11.cp.hostnet.nl:3306/db30984_stayc";
        String user = "u30984_fhd";
        String password = "stayconnected";
        
        List<String> cits = new ArrayList<String>();
        List<String> citsCompare = new ArrayList<String>();
        
        float match;
        
        int tot;
        int totCompare;
        
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT * FROM profiles");
            rs = pst.executeQuery();
            
            con.setAutoCommit(false);

            while(rs.next()) {
            	cits.clear();
            	tot = 0;
            	
            	String accountName = rs.getString(1);
                String sex = rs.getString(2);
                int activity = Integer.parseInt(rs.getString(3));
                String cities = rs.getString(4);
                
                System.out.println(accountName);
                
                cities = cities.substring(1, cities.length()-1);
            	String[] citss = cities.split(",");
            	for(String loc: citss) {
            		if(isInteger(loc.trim())) {
        				tot += Integer.parseInt(loc.trim());
        				System.out.println("Tot: " + tot);
        			}
            		if(!cits.contains(loc.trim()) || isInteger(loc.trim())) {
            			cits.add(loc.trim());
            		}
            	}
                
                pstl = con.prepareStatement("SELECT * FROM profiles WHERE accountName != '" + accountName + "'");
                rsl = pstl.executeQuery();
                
                while(rsl.next()) {
                	citsCompare.clear();
                	match = 0.0f;
                	totCompare = 0;
                	
                	String accountNameCompare = rsl.getString(1);
                    String sexCompare = rsl.getString(2);
                    int activityCompare = Integer.parseInt(rsl.getString(3));
                    String citiesCompare = rsl.getString(4);
                    
                    System.out.println(accountNameCompare);
                    
                    if(sex.equals(sexCompare)) {
                    	System.out.println("Sex is the same continueing");
                    	match = 0;
                    	break;
                    }
                    
                    float activityPer = 0.0f;
                    activityPer = (1.0f/(Math.abs(activity-activityCompare)+1))*100.0f;
                    System.out.println("ActivityPer: " + activityPer);
                    
                    citiesCompare = citiesCompare.substring(1, citiesCompare.length()-1);
                	String[] citssCompare = citiesCompare.split(",");
                	for(String loc: citssCompare) {
            			if(isInteger(loc.trim())) {
            				totCompare += Integer.parseInt(loc.trim());
            				System.out.println("TotCompare: " + totCompare);
            			}
                		if(!citsCompare.contains(loc.trim()) || isInteger(loc.trim())) {
                			citsCompare.add(loc.trim());
                		}
                	}
                	
                	
                	float verschil = 0.0f;
                	float totaal = 0.0f;
                	float aantal = 0.0f;
                	
                	if(citsCompare.size() < cits.size()) {
                		for(String loc: cits) {
                			for(String locc: citsCompare) {
                				if(loc.equalsIgnoreCase(locc) && !isInteger(locc.trim())) {
                					int i = cits.indexOf(loc);
                					int k = citsCompare.indexOf(locc);
            						float n = Float.valueOf(cits.get(i+1));
            						float m = Float.valueOf(citsCompare.get(k+1));
            						float citPer = (n*100.0f)/tot;
            						float citCompPer = (m*100.0f)/totCompare;
            						verschil += Math.abs(citPer - citCompPer);
            						aantal++;
            						totaal += 100;
                				}
                				else {
                					verschil += 100;
                					aantal++;
                				}
                			}
                		}
                	}
                	else {
                		for(String loc: citsCompare) {
                			for(String locc: cits) {
                				if(loc.equalsIgnoreCase(locc) && !isInteger(locc.trim())) {
                					int i = cits.indexOf(locc);
                					int k = citsCompare.indexOf(loc);
            						float n = Float.valueOf(cits.get(i+1));
            						float m = Float.valueOf(citsCompare.get(k+1));
            						float citPer = (n*100.0f)/tot;
            						float citCompPer = (m*100.0f)/totCompare;
            						verschil += Math.abs(citPer - citCompPer);
            						aantal++;
            						totaal += 100;
                				}
                				else {
                					verschil += 100;
                					aantal++;
                				}
                			}
                		}
                	}
                	System.out.println("aantal: " + aantal);
                	System.out.println("verschil: " + verschil);
                	System.out.println("totaal: " + totaal);
                	
                	float citsPer = (verschil/aantal)/totaal;
                	System.out.println("citsPer: " + citsPer);
                	
                	match = (activityPer + citsPer) / 2;
                	System.out.println("match: " + match);
                	
                	//Send to database
                }
            }
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
                if (pstl != null) {
                    pstl.close();
                }
                if (st != null) {
                    st.close();
                }
                if (rsl != null) {
                    rsl.close();
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
    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
}