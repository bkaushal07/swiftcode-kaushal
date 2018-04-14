package services;

import com.fasterxml.jackson.databind.JsonNode;
import data.NewsAgentResponse;
import play.libs.ws.WS;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class NewsAgentService {
    public NewsAgentResponse getNewsAgentResponse(String query, UUID sessionId){
        NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
        try{
            //creating websocket for dialogflow(NLP) api
            WSRequest queryRequest = WS.url("http:/api.api.ai/api/query");
            //set query params that has to be sent to the api
            CompletionStage<WSResponse> responsePromise =queryRequest
                    .setQueryParameter("v","20150910")
                    .setQueryParameter("query",query)
                    .setQueryParameter("lang","en")
                    .setQueryParameter("sessionId", sessionId.toString())
                    .setQueryParameter("timezone","2018-13-04T16:57:23+0530")
                    .setHeader("Authorization","Bearer 054a388ef08e46c3beb61cd9a12dd13f")
                    .get();
            //collect the response from api
            //this api will return keyword, source and category
            JsonNode response = responsePromise.thenApply(WSResponse::asJson).toCompletableFuture().get();
            newsAgentResponse.query = response.get("result").get("parameters").get("keyword").asText().isEmpty() ?
                    (response.get("result").get("parameters").get("source").asText().isEmpty()
                            ? response.get("result").get("parameters").get("category").asText()
                            : response.get("result").get("parameters").get("source").asText() )
                    : response.get("result").get("parameters").get("keyword").asText() ;
        }
        catch (Exception e){
            e.printStackTrace();

        } /*catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        return newsAgentResponse;
    }

}
