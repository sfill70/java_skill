package LinkActionNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;


public class LinkRecursiveActionNode extends RecursiveAction {
    private String baseLink;
    private Node parent;

    private static String baseUrl;
    private static Set<String> uniqueURL = new ConcurrentSkipListSet<>();
    private List<LinkRecursiveActionNode> actionList = new CopyOnWriteArrayList<>();
    private static TreeNode treeNode = new TreeNode();
    private static String[] filterArray;

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
    private static final String REFERRER = "https://www.google.com/";

    private static final Logger LOGGER = LogManager.getLogger(LinkRecursiveActionNode.class);
    private static final Marker INVALID_LINE_MARKER = MarkerManager.getMarker("VIEW_INVALID");
    private static final Marker VIEW_FILEPATH_MARKER = MarkerManager.getMarker("VIEW_FILEPATH");


    public LinkRecursiveActionNode(String baseLink, Node parent) {
        this.baseLink = baseLink;
        this.parent = parent;
    }

    public LinkRecursiveActionNode(String baseLink, Node parent, String... filter) {
        this.baseLink = baseLink;
        this.parent = parent;
        baseUrl = baseLink;
        uniqueURL.add(baseLink);
        treeNode.setParent(parent);
        filterArray = filter;
    }


    public static TreeNode getTreeNode() {
        return treeNode;
    }

    public static Set<String> getUniqueURL() {
        return uniqueURL;
    }

    @Override
    protected void compute() {
        getLinks(baseLink, parent);
    }

    private void getLinks(String url, Node parent) {
        try {
            Elements links = getElements(url);

            if (links.isEmpty()) {
                return;
            }
            for (Element link : links) {
                String href = link.attr("abs:href");
                if (href.contains(baseUrl) /*&& href.contains(url)*/ && !uniqueURL.contains(href) && filter(href)) {
                    uniqueURL.add(href);
                    Node child = new Node(href);
                    parent.addChild(child);
                    Thread.sleep(100);
                    System.out.println(href /*+ " - " + Thread.currentThread().getName()*/);
                    LinkRecursiveActionNode action = new LinkRecursiveActionNode(href, child);
                    action.fork();
                    actionList.add(action);
                }
            }
        } catch (Exception e) {
            LOGGER.error(" fork - " + e + Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        for (LinkRecursiveActionNode action : actionList)
            try {
                action.join();
            } catch (Exception e) {
                LOGGER.error(action + " join - " + e + Arrays.toString(e.getStackTrace()));
                LOGGER.error(VIEW_FILEPATH_MARKER, action);
                e.printStackTrace();
            }


    }

    private boolean filter(String href) {
        for (String st : filterArray
        ) {
            if (href.contains(st)) {
                return false;
            }
        }
        return true;
    }

    private Elements getElements(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).
                    userAgent(USER_AGENT).
                    referrer(REFERRER).get();
        } catch (Exception e) {
            e.printStackTrace();
            return new Elements(0);
        }
        return doc.select("a[href]");
    }

}
