import java.util.TreeMap;

public class Model {

    private TreeMap<String, Double> tagFrequency;
    private TreeMap<String, TreeMap<String, Double>> prevActualMap;
    private TreeMap<String, TreeMap<String, Double>> tagTockenMap;
    private String[] brownCorpusTags;


    public Model() {
        super();
    }

    public Model(TreeMap<String, Double> tagFrequency, TreeMap<String, TreeMap<String, Double>> prevActualMap,
                 TreeMap<String, TreeMap<String, Double>> tagTockenMap, String[] brownCorpusTags) {
        super();
        this.tagFrequency = tagFrequency;
        this.prevActualMap = prevActualMap;
        this.tagTockenMap = tagTockenMap;
        this.brownCorpusTags = brownCorpusTags;
    }


    public TreeMap<String, Double> getTagFrequency() {
        return tagFrequency;
    }

    public void setTagFrequency(TreeMap<String, Double> tagFrequency) {
        this.tagFrequency = tagFrequency;
    }

    public TreeMap<String, TreeMap<String, Double>> getPrevActualMap() {
        return prevActualMap;
    }

    public void setPrevActualMap(TreeMap<String, TreeMap<String, Double>> prevActualMap) {
        this.prevActualMap = prevActualMap;
    }

    public TreeMap<String, TreeMap<String, Double>> getTagTockenMap() {
        return tagTockenMap;
    }

    public void setTagTockenMap(TreeMap<String, TreeMap<String, Double>> tagTockenMap) {
        this.tagTockenMap = tagTockenMap;
    }

    public String[] getBrownCorpusTags() {
        return brownCorpusTags;
    }

    public void setBrownCorpusTags(String[] brownCorpusTags) {
        this.brownCorpusTags = brownCorpusTags;
    }


}
