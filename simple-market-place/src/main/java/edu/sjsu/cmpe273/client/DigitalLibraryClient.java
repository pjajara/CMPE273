package edu.sjsu.cmpe273.client;
import javax.jms.*;

import java.util.*;

import javax.naming.*;

public class DigitalLibraryClient {
	private Connection connection;
	private Session session;
	private Topic counterTopic;
	private MessageConsumer consumer;
	private Topic replyTopic;
	private String Yash;  
	private int i;
	private int Pawan;
	
	public static void main(String args[])
	{
		new DigitalLibraryClient();
	}
	
		
	public int IssueMovie() throws JMSException
	{
		int sum;
		MessageProducer MP = session.createProducer(counterTopic);
		TextMessage TM = session.createTextMessage("issuemovie");
		
		replyTopic = session.createTemporaryTopic();		
		consumer = session.createConsumer( replyTopic );
		
		TM.setJMSReplyTo(replyTopic);
		MP.send(TM);
		TextMessage Reply = (TextMessage)consumer.receive();
		
		sum = Integer.parseInt( Reply.getText() );
		
		return sum;
	}
	
	public int GetSumIssuedCopies() throws JMSException
	{
		int sum;
		MessageProducer MP = session.createProducer(counterTopic);
		TextMessage TM = session.createTextMessage("getsum_issued_movie");
		
		replyTopic = session.createTemporaryTopic();		
		consumer = session.createConsumer( replyTopic );
		
		TM.setJMSReplyTo(replyTopic);
		MP.send(TM);
		TextMessage Reply = (TextMessage)consumer.receive();
		
		sum = Integer.parseInt( Reply.getText() );
		
		return sum;
	}
	
	public DigitalLibraryClient()
	{
		try
		{
		    Properties properties = new Properties();
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		    properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
		    properties.put(Context.PROVIDER_URL, "localhost");
			
			InitialContext jndi = new InitialContext(properties);
			ConnectionFactory conFactory = (ConnectionFactory)jndi.lookup("XAConnectionFactory");
			connection = conFactory.createConnection();
			
			session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
			counterTopic = (Topic)jndi.lookup("CounterTopic");
			
			connection.start();
			
			
			
				System.out.println("Issue movies three times ");
				IssueMovie();
				IssueMovie();
				IssueMovie();
			
						
			System.out.println("Sum of Total Issued movies = "+ GetSumIssuedCopies() );
			

		}
		catch(NamingException NE)
		{
			System.out.println("Naming Exception: "+NE);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}
	}
	

}
