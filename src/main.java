package tum;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public class main
{
  public static void main(String[] args)
    throws IOException
  {
    File file = new File("params.xml");
    AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
    
    ClientConfiguration conf = null;
    if (file.exists()) {
      try
      {
        conf = init(file);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    String mode = args[0];
    String path = args[1] + ":/xyz";
    
    long st = printDate();
    
    String regex1 = "tum";
    Pattern p1 = Pattern.compile(regex1, 2);
    String regex2 = "mokuroku";
    Pattern p2 = Pattern.compile(regex2, 2);
    String regex3 = "cocotile";
    Pattern p3 = Pattern.compile(regex3, 2);
    if (check(p1, mode).booleanValue())
    {
      AmazonDynamoDB Dynamo = new AmazonDynamoDBClient(credentialsProvider, conf);
      AmazonKinesis kinesis = new AmazonKinesisClient(credentialsProvider, conf);
      AmazonS3 s3 = new AmazonS3Client(credentialsProvider, conf);
      
      TaskManager.tileSearchMain(path, Dynamo, kinesis, s3);
    }
    if (check(p2, mode).booleanValue()) {
      try
      {
        DynamoDB.mokuroku(path, conf);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    if (check(p3, mode).booleanValue()) {
      DynamoDB.cocotile(path, conf);
    }

	printDate();

    //long ed = printDate();
    //Calendar c = Calendar.getInstance();
    //long sa = ed - st - c.getTimeZone().getRawOffset();
    //c.setTimeInMillis(sa);
    
    //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    
    //String[] s = sdf.format(c.getTime()).split(":");
    //int hour = Integer.parseInt(s[0]);
    //int minute = Integer.parseInt(s[1]);
    
    //System.out.println("total time" + hour + ":" + minute);
  }
  
  private static Boolean check(Pattern p, String target)
  {
    Matcher m = p.matcher(target);
    if (m.find()) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  static long printDate()
  {
    Calendar now = Calendar.getInstance();
    
    int year = now.get(1);
    int month = now.get(2) + 1;
    int date = now.get(5);
    int hour = now.get(11);
    int minute = now.get(12);
    int second = now.get(13);
    
    String user = System.getProperty("user.name");
    
    System.out.println("user:" + user);
    System.out.print("system start:" + year + "/" + month + "/" + date + 
      "\t");
    System.out.printf("%02d:%02d:%02d%n", new Object[] { Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second) });
    
    long sa = now.getTimeInMillis();
    return sa;
  }
  
  private static ClientConfiguration init(File file)
    throws Exception
  {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document doc = builder.parse(file);
    
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();
    
    String host = xpath.evaluate("/Params/Proxy/Host/text()", doc);
    String port = xpath.evaluate("/Params/Proxy/Port/text()", doc);
    
    ClientConfiguration conf = new ClientConfiguration();
    if ((!host.equals("")) && (!port.equals("")))
    {
      conf.setProtocol(Protocol.HTTPS);
      conf.setProxyHost(host);
      conf.setProxyPort(Integer.parseInt(port));
      
      return conf;
    }
    return conf;
  }
}
