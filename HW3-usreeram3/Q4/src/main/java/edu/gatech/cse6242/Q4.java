package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4 {

	public static class DegreeMapper
    extends Mapper<Object, Text, Text, Text>{

    private final static IntWritable one = new IntWritable(1);
    private IntWritable node = new IntWritable();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

      String tokens[] = value.toString().split("\t");



      context.write(new Text(tokens[0]), new Text(new String("1")));
      context.write(new Text(tokens[1]),new Text( new String("-1")));

      
    }
  }

  public static class DegreeReducer
       extends Reducer<Text,Text,Text,Text> {
    

    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (Text val : values) {
        sum += Integer.parseInt(val.toString());
      }

      String sum1=sum+"";
      
      context.write(new Text(key), new Text(sum1));
    }
  }


  public static class FrequencyMapper
    extends Mapper<Object, Text, Text, Text>{

    private final static IntWritable one = new IntWritable(1);
    private IntWritable node = new IntWritable();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      

      String tokens[] = value.toString().split("\t");

      context.write( new Text(tokens[1]), new Text(new String("1")) );
      
    }
  }

  public static class FrequencyReducer
       extends Reducer<Text,Text,Text,Text> {
    private IntWritable result = new IntWritable();

   public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (Text val : values) {
        sum += Integer.parseInt(val.toString());
      }

      String sum1=sum+"";
      
      context.write(new Text(key), new Text(sum1));
    }
  }



  public static void main(String[] args) throws Exception {
    
    Configuration conf = new Configuration();
    Job job1 = Job.getInstance(conf, "job1");

    job1.setJarByClass(Q4.class);
    job1.setMapperClass(DegreeMapper.class);
    job1.setCombinerClass(DegreeReducer.class);
    job1.setReducerClass(DegreeReducer.class);
    job1.setOutputKeyClass(Text.class); 
    job1.setOutputValueClass(Text.class);



    Path Outputpath = new Path("interim");

    FileInputFormat.addInputPath(job1, new Path(args[0]));
    FileOutputFormat.setOutputPath(job1, Outputpath);
    //System.exit(job1.waitForCompletion(true) ? 0 : 1);

    Outputpath.getFileSystem(conf).delete(Outputpath);
    job1.waitForCompletion(true);

    //Second Job
    Job job2 = Job.getInstance(conf, "job2");
 

    job2.setJarByClass(Q4.class);
    job2.setMapperClass(FrequencyMapper.class);
    job2.setCombinerClass(FrequencyReducer.class);
    job2.setReducerClass(FrequencyReducer.class);
    job2.setOutputKeyClass(Text.class);
    job2.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job2, new Path("interim"));
    FileOutputFormat.setOutputPath(job2, new Path(args[1]));

    System.exit(job2.waitForCompletion(true) ? 0 : 1);





  }
}
























	