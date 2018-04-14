package services;

import com.fasterxml.jackson.databind.JsonNode;
import data.FeedResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class FeedService {
    public FeedResponse getFeedResponse(String query){
        FeedResponse feedResponseObject = new FeedResponse();
        try{
            //creating websocket google news api
            WSRequest feedRequest = WS.url("http://news.google.com/news");
            //set the params like keyword(retrieved from dialogflow) to the google news api
            CompletionStage<WSResponse> responsePromise =feedRequest
                    .setQueryParameter("q","ipl")
                    .setQueryParameter("output","rss")
                    .get();
            Document feedResponse = responsePromise.thenApply(WSResponse::asXml).toCompletableFuture().get();
            //retrieving 10th item in the channel
            Node item = feedResponse.getFirstChild().getFirstChild().getChildNodes().item(10);
            //retrieving title, desc, pubdate from 10th item in the channel retireved
            feedResponseObject.title = item.getChildNodes().item(0).getFirstChild().getNodeValue();
            feedResponseObject.description = item.getChildNodes().item(4).getFirstChild().getNodeValue();
            feedResponseObject.pubDate = item.getChildNodes().item(3).getFirstChild().getNodeValue();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
