import java.util.Arrays;

public class Algoritme
{
  
	
	

    public static void main( String[] args )
    {
        String []input, input2;
        input = new String[2];
        input[0]= "(Location[mProvider=fused,mTime=1371560223414," +
        		"mLatitude=52.3555921,mLongitude=4.9571157,mHasAltitude=false," +
        		"mAltitude=0.0,mHasSpeed=false,mSpeed=0.0,mHasBearing=false," +
        		"mBearing=0.0,mHasAccuracy=true,mAccuracy=22.273," +
        		"mExtras=Bundle[mParcelledData.dataSize=360]]);" +
        		"(Address[Science Park Amsterdam 500 Amsterdam The Netherlands])";
    	input[1]="(Location[mProvider=fused,mTime=1371560223414," +
        		"mLatitude=52.3555921,mLongitude=4.9571157,mHasAltitude=false," +
        		"mAltitude=0.0,mHasSpeed=false,mSpeed=0.0,mHasBearing=false," +
        		"mBearing=0.0,mHasAccuracy=true,mAccuracy=22.273," +
        		"mExtras=Bundle[mParcelledData.dataSize=360]]);" +
        		"(Address[Science Park Amsterdam 500 Amsterdam The Netherlands])";
    	input2 = new String[2];
    	
    	double matchRate;
        matchRate = Compare(input,input2,input.length,input2.length);
        System.out.printf( "Matchrate is %f!",matchRate );
    }
    public static double Compare(String[] first,String[] second,int length1,int length2){
		
    	int i = 0;
    	int j = 0;
    	double matches = 0;
    	double tries = 0;
    	double matchRate = 0.0;
    	for(;i<length1;){
    		for(;;){
    			if(first[i].equals(second[j])){
    				matches++;
    			}
    			tries++;
    		}
    	}
    	matchRate = matches/tries;
    	return matchRate;
    	
    }
}
