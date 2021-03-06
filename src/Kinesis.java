package tum;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import org.apache.commons.lang.RandomStringUtils;

public class Kinesis
{
  static int cc = 0;
  
  public static void kinesisMain(String filePath, String destination, AmazonKinesis kinesis)
  {
    try
    {
      putToKinesis(filePath, destination, kinesis);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void putToKinesis(String filePath, String destination, AmazonKinesis kinesis)
    throws IOException
  {
    Region reg = Region.getRegion(Regions.AP_NORTHEAST_1);
    
    kinesis.setRegion(reg);
    
    File f = new File(filePath);
    
    int length = (int)f.length();
    byte[] buf = new byte[length];
    FileInputStream fs = new FileInputStream(filePath);
    fs.read(buf);
    fs.close();
    
    ByteBuffer bb = ByteBuffer.wrap(buf);
    
    PutRecordRequest putRecordRequest = new PutRecordRequest();
    
    putRecordRequest.setStreamName(input your stream);
    
    putRecordRequest.setData(bb);
    
    String rand = RandomStringUtils.randomAlphanumeric(5);
    putRecordRequest.setPartitionKey(rand + destination + f.getName());
    kinesis.putRecord(putRecordRequest);
    
    cc += 1;
    
    System.out.println(cc + "\tkinesis Put:" + filePath);
  }
}
