package actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.FeedResponse;
import data.Message;
import data.NewsAgentResponse;
import services.FeedService;
import services.NewsAgentService;

import java.util.UUID;

public class MessageActor extends UntypedActor
{
    private final ActorRef out;

    //Self reference the Actor
    public MessageActor(ActorRef out)
    {
        //Define another actor Reference
        this.out = out;
    }

    //PROPS
    public static Props props(ActorRef out)
    {
        return Props.create(MessageActor.class, out);
    }

    //Object of FeedService
    private FeedService feedService = new FeedService();

    //Object of NewsAgentService
    private NewsAgentService newsAgentService = new NewsAgentService();
    private NewsAgentResponse newsAgentResponse = new NewsAgentResponse();
    @Override
    public void onReceive(Object message) throws Throwable
    {
        // Send back the response
        // create object of feedresponse,objectmapper,message
        ObjectMapper objectMapper = new ObjectMapper();
        Message messageObject = new Message();
        if (message instanceof String)
        {

            messageObject.text = (String)message;
            messageObject.sender = Message.Sender.USER;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
            String query = newsAgentService.getNewsAgentResponse("Find " + message, UUID.randomUUID()).query;
            FeedResponse feedResponse = feedService.getFeedByQuery(query);
            messageObject.text = (feedResponse.title == null) ? "No results found" : "Showing results for: " + query;
            messageObject.feedResponse = feedResponse;
            messageObject.sender = Message.Sender.BOT;
            out.tell(objectMapper.writeValueAsString(messageObject), self());
        }
    }
}