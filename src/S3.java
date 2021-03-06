package tum;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.PrintStream;

public class S3
{
  static int count = 0;
  static AmazonDynamoDB Dynamo;
  static AmazonS3 S3;
  
  public static void putToS3(String path, String filepath, AmazonS3 s3)
  {
    String bucketName = input your bucket name;
    File file = new File(filepath);
    count += 1;
    s3.putObject(new PutObjectRequest(bucketName, path + file.getName(), file));
    System.out.println(count + "\tS3 Put:" + path + file.getName());
  }
}
