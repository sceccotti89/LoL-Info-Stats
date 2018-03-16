
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main
{
    private static final String USER_AGENT = "Mozilla/5.0";
    
    private static final String SUMMONER_ID = "id";
    
    private static final String QUEUE_TYPE = "queueType";
    private static final String TIER = "tier";
    private static final String RANK = "rank";
    
    private static final String BRAZIL = "br1";
    private static final String EUROPE_NORTH_EAST = "eun1";
    private static final String EUROPE_WEST = "euw1";
    private static final String JAPAN = "jp1";
    private static final String KOREAN = "kr";
    private static final String LATIN_AMERICAN_NORTH = "la1";
    private static final String LATIN_AMETICAN_SOUTH = "la2";
    private static final String NORTH_AMERICA = "na1";
    // Note: The NA region has two associated platform values - NA and NA1.
    // Older summoners will have the NA platform associated with their account,
    // while newer summoners will have the NA1 platform associated with their account.
    private static final String OCEANIA = "oc1";
    private static final String TURKEY = "tr1";
    private static final String RUSSIA = "ru";
    
    private static final Map<String,String> COUNTRIES = new HashMap<>( 11 );
    
    public static void main( String argv[] ) throws Exception
    {
        if (argv.length == 0) {
            System.err.println( "Invalid number of arguments: missing the API key parameter." );
            throw new Exception();
        }
        
        createCoutryMap();
        
        final String key = argv[0];
        final String country = COUNTRIES.get( "EUW" );
        
        long summonerId = getSummonerId( country, key );
        getSummonerRankStats( key, country, summonerId );
    }
    
    private static void createCoutryMap()
    {
        COUNTRIES.put( "BR",   BRAZIL );
        COUNTRIES.put( "EUNE", EUROPE_NORTH_EAST );
        COUNTRIES.put( "EUW",  EUROPE_WEST );
        COUNTRIES.put( "JP",   JAPAN );
        COUNTRIES.put( "KR",   KOREAN );
        COUNTRIES.put( "LAN",  LATIN_AMERICAN_NORTH );
        COUNTRIES.put( "LAS",  LATIN_AMETICAN_SOUTH );
        COUNTRIES.put( "NA",   NORTH_AMERICA );
        COUNTRIES.put( "OCE",  OCEANIA );
        COUNTRIES.put( "TU",   TURKEY );
        COUNTRIES.put( "RU",   RUSSIA );
    }
    
    private static long getSummonerId( String country, String key ) throws Exception
    {
        final String summonerName = "TheFallenaagba";
        final String url = "https://" + country + ".api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summonerName + "?api_key=" + key;
        
        URL obj = new URL( url );
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod( "GET" );
        
        // Add request header.
        con.setRequestProperty( "User-Agent", USER_AGENT );
        
        //int responseCode = con.getResponseCode();
        //System.out.println( "\nSending 'GET' request to URL : " + url );
        //System.out.println( "Response Code : " + responseCode );
        
        BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
        String inputLine;
        StringBuilder response = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null) {
            response.append( inputLine );
        }
        in.close();
        
        JSONObject result = new JSONObject( response.toString() );
        return result.getLong( SUMMONER_ID );
    }
    
    private static void getSummonerRankStats( String key, String country, long summonerId ) throws Exception
    {
        final String url = "https://" + country + ".api.riotgames.com/lol/league/v3/positions/by-summoner/" + summonerId + "?api_key=" + key;
        
        URL obj = new URL( url );
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod( "GET" );
        
        // Add request header.
        con.setRequestProperty( "User-Agent", USER_AGENT );
        
        //int responseCode = con.getResponseCode();
        //System.out.println( "\nSending 'GET' request to URL : " + url );
        //System.out.println( "Response Code : " + responseCode );
        
        BufferedReader in = new BufferedReader( new InputStreamReader( con.getInputStream() ) );
        String inputLine;
        StringBuilder response = new StringBuilder();
        
        while ((inputLine = in.readLine()) != null) {
            response.append( inputLine );
        }
        in.close();
        
        JSONArray results = new JSONArray( response.toString() );
        for (int i = 0; i < results.length(); i++) {
            JSONObject value = results.getJSONObject( i );
            // RANKED_FLEX_SR is just Flex.
            final String queue = value.getString( QUEUE_TYPE );
            final String tier = value.getString( TIER );
            final String rank = value.getString( RANK );
            System.out.println( "Queue: " + queue + ", Tier: " + tier + ", Rank: " + rank );
        }
    }
}
