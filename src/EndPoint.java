import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by royd1990 on 2/23/17.
 */
public class EndPoint {
    private HashMap<Integer,Integer> cacheLatency;
    private HashMap<Integer,Integer> videoRequest;
    private int dataCenterLatency;
    //private int savedLatency=0;

    public EndPoint(HashMap<Integer,Integer> cacheLatency,HashMap<Integer,Integer> videoRequest){
        this.cacheLatency=cacheLatency;
        this.videoRequest=videoRequest;
        this.dataCenterLatency=cacheLatency.get(-1);
    }

    public void calculateBestLatency(){
        int latencyDiff;
        Iterator videoRequestIterator = videoRequest.entrySet().iterator();
        while(videoRequestIterator.hasNext()){
            Iterator cacheLatencyIterator = cacheLatency.entrySet().iterator();
            while(cacheLatencyIterator.hasNext()){
                HashMap.Entry pair = (HashMap.Entry)cacheLatencyIterator.next();
                HashMap.Entry videoPair = (HashMap.Entry)videoRequestIterator.next();
                latencyDiff=(dataCenterLatency-(int)pair.getValue())*(int)videoPair.getValue();
            }
            //Put a value for each endpoint, for each video like this  EndPoint, Video, EffectiveBest Latency, Cache
        }
    }

}