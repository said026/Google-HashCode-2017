import java.util.*;

/**
 * Created by royd1990 on 2/23/17.
 */
public class VideoCache {
    private int id;
    private int cacheLimit;
    private ArrayList<Integer> videos=new ArrayList<Integer>();
    private HashMap<Integer,Integer> cacheTable = new HashMap<Integer,Integer>();
    private HashMap<Integer,Integer> videoSize;
    public VideoCache(int id, int cacheLimit,HashMap<Integer,Integer> videoSize){
        this.id=id;
        this.cacheLimit=cacheLimit;
        this.videoSize=videoSize;
    }

    public void PopulateCacheTable(int videos,int latency){
           cacheTable.put(videos,latency);
    }

    public ArrayList<Integer> computeCacheTable(){
            int sum=0;
            LinkedHashMap<Integer,Integer> sortedMap = sortHashMapByValues(cacheTable);
            Iterator sortedMapIterator = sortedMap.entrySet().iterator();
            while(sortedMapIterator.hasNext()){
                HashMap.Entry pair = (HashMap.Entry)sortedMapIterator.next();
                if(sum<cacheLimit){
                    sum+=videoSize.get(pair.getKey());
                    videos.add((int)pair.getKey());
                }
            }
            return videos;
    }

    private LinkedHashMap<Integer, Integer> sortHashMapByValues(
            HashMap<Integer, Integer> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }


}
