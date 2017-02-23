import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String line;
        int lineNb = 0;

        int videoNb = 0;
        int endpointNb = 0;
        int requests = 0;
        int cachesNb = 0;
        int cacheSize = 0;

        int endpointCachesNb = 0;

        // map for the videos (video numebr, size of video)
        HashMap<Integer, Integer> videosMap = new HashMap<Integer, Integer>();

        // map for the endpoint latencies (destination, latency)
        HashMap<Integer, Integer> endpointLatencies = new HashMap<Integer, Integer>();

        // list that stores latency hashmap of each endpoint
        List<HashMap<Integer,Integer>> endpointLatenciesList = new ArrayList<HashMap<Integer,Integer>>();

        // map for the endpoint requests (video, nb requests)
        HashMap<Integer, Integer> endpointRequests = new HashMap<Integer, Integer>();

        // list that stores endpoint requests
        HashMap<Integer, HashMap<Integer, Integer>> endpointRequestsList = new HashMap<Integer, HashMap<Integer, Integer>>();

        int[] intArray;

        String filename = "src/kittens.in";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            while ((line = reader.readLine()) != null) {

                // read the line and parse it to an int array
                intArray = toIntArray(line.split("\\s+"));

                if (lineNb == 0) {
                    // extract the first line information
                    videoNb = intArray[0];
                    endpointNb = intArray[1];
                    requests = intArray[2];
                    cachesNb = intArray[3];
                    cacheSize = intArray[4];
                } else if (lineNb == 1) {
                    // extract the videos size's
                    for (int j=0; j<videoNb; j++) {
                        videosMap.put(j, intArray[j]);
                    }
                }

                if (lineNb > 2) {
                    // if it is a endpoint to destination latency
                    if (intArray.length == 2) {
                        if (endpointCachesNb == 0) {
                            // parsing the latencies
                            endpointLatencies.put(-1, intArray[0]);
                            endpointCachesNb = intArray[1];
                        } else {
                            // extracting the latencies to each cache
                            if (endpointCachesNb > 0) {
                                // add the latency to the endpointLatencies hashmap
                                endpointLatencies.put(intArray[0], intArray[1]);
                                endpointCachesNb--;
                            } else {
                                // add the endpointLatencies hashmap to the endpointList
                                endpointLatenciesList.add(endpointLatencies);
                            }
                        }
                    }
                    if (intArray.length == 3){
                        // dealing with the requests
                        int video = intArray[0];
                        int ep = intArray[1];
                        int rqst = intArray[2];

                        if (endpointLatenciesList.contains(ep)) {
                            endpointRequestsList.get(ep).put(video, rqst);
                        } else {
                            endpointRequestsList.put(ep, new HashMap<>());
                            endpointRequestsList.get(ep).put(video, rqst);
                        }

                    }
                }
                lineNb++;
            }
            reader.close();


        }
        catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }

        // list of VideoCaches
        List<VideoCache> videoCachesList = new ArrayList<VideoCache>();

        for(int i=0;i<cachesNb; i++) {
            videoCachesList.add(new VideoCache(i,cacheSize, videosMap));
        }

        List<EndPoint> endpointList = new ArrayList<EndPoint>();
        // list of the endpoints
        HashMap<VideoCache,Integer> epLatencies = new HashMap<>();

        for(int i=0;i<endpointNb; i++) {
            HashMap<Integer, Integer> epl = endpointLatenciesList.get(i);

            for(Map.Entry<Integer, Integer> entry : epl.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                VideoCache v = videoCachesList.get(key);
                epLatencies = new HashMap<VideoCache,Integer>();

                epLatencies.put(v, value);
            }

            endpointList.add(new EndPoint(epLatencies, endpointRequestsList.get(i), i));
        }

        for(int i=0;i<endpointList.size();i++) {
            endpointList.get(i).calculateBestLatency();
        }
        ArrayList<Integer> vidz = new ArrayList<Integer>();
        HashMap<Integer, Integer> hmp = new HashMap<Integer, Integer>();

        for (int i=0; i<videoCachesList.size();i++) {
            vidz = videoCachesList.get(i).computeCacheTable();
            for (int j=0; j<vidz.size(); j++) {
                hmp.put(i, vidz.get(j));
            }
        }

        for (Map.Entry<Integer, Integer> entry : hmp.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();

            System.out.println(key);
            System.out.println(value);
            System.out.println("------------");
        }
    }

    public static int[] toIntArray(String[] numberStrs) {
        int[] numbers = new int[numberStrs.length];
        for(int i = 0;i < numberStrs.length;i++) {
            numbers[i] = Integer.parseInt(numberStrs[i]);
        }
        return numbers;
    }
}
