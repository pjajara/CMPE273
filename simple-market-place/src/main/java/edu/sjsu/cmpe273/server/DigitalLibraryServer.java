package edu.sjsu.cmpe273.server;
import java.util.Properties;

import javax.jms.*;
import javax.naming.*;

public class DigitalLibraryServer implements MessageListener{
private movies counter = new movies();
	
	private Connection connection;
	private Session session;
	private Topic counterTopic;
	private MessageConsumer consumer;

	public static void main(String args[])
	{
		new DigitalLibraryServer();
	}
	
	public void sendReply(Message request, int sum)
	{
		try
		{
			MessageProducer MP = session.createProducer(null);
			Destination reply = request.getJMSReplyTo();
			TextMessage TM = session.createTextMessage();
			TM.setText(""+sum);
			MP.send(reply, TM);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}
	}
	
	public void onMessage(Message message)
	{
		TextMessage TM = (TextMessage)message;
		
		try
		{
		
			if( TM.getText().equalsIgnoreCase("issuemovie"))
			{
				int newsum = counter.issuemovie();
				sendReply(message, newsum);
			}
			else if( TM.getText().equalsIgnoreCase("getsum_issued_movie"))
			{
				int sum = counter.getsum_issued_movies();
				sendReply(message, sum);
			}
			
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}
	}
	
	public DigitalLibraryServer()
	{
		try
		{
		    Properties properties = new Properties();
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		    properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
		    properties.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		    
			InitialContext jndi = new InitialContext(properties);
			ConnectionFactory conFactory = (ConnectionFactory)jndi.lookup("XAConnectionFactory");
			connection = conFactory.createConnection();
			
			session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
			
			try
			{
				counterTopic = (Topic)jndi.lookup("CounterTopic");
			}
			catch(NamingException NE1)
			{
				System.out.println("NamingException: "+NE1+ " : Continuing anyway...");
			}
			
			if( null == counterTopic )
			{
				counterTopic = session.createTopic("CounterTopic");
				jndi.bind("CounterTopic", counterTopic);
			}
			
			consumer = session.createConsumer( counterTopic );
			consumer.setMessageListener(this);
			System.out.println("Server started waiting for client requests");
			connection.start();
		}
		catch(NamingException NE)
		{
			System.out.println("Naming Exception: "+NE);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
			JMSE.printStackTrace();
		}
	}
}
