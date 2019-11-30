package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.*;
import java.util.*;


public class Q1 {
	  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{


    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
  
        String tokens[] = value.toString().split("\t");

        if(tokens.length==3)
        {

        String address = tokens[0] ;
        String weight = tokens[1]+","+tokens[2]; 
        
        context.write(new Text(address),new Text(weight));
    }
    //  }
    }
  }

  public static class IntSumReducer
       extends Reducer< Text, Text, Text, Text> {
   // private String result = "";

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
    {
      int max = -1;
      int target = Integer.MAX_VALUE;

      for (Text val : values) 
      {
      	
      	String[] line1= val.toString().split(",");
      	int max1=Integer.parseInt(line1[1]);
      	int target1=Integer.parseInt(line1[0]);

        if(max1 > max) 
        	{
        	 max = max1;
        	 target=target1;
        	
        	}
        if(max1== max && target1 < target)
        {
        	max = max1;
        	target=target1;

        }


      }
      String result = target+","+max;
      
      context.write(new Text(key), new Text(result));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");

    /* TODO: Needs to be implemented */
    job.setJarByClass(Q1.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);

    
  }
}
