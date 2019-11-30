// Databricks notebook source
// Q2 [25 pts]: Analyzing a Large Graph with Spark/Scala on Databricks

// STARTER CODE - DO NOT EDIT THIS CELL
import org.apache.spark.sql.functions.desc
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import spark.implicits._

// COMMAND ----------

// STARTER CODE - DO NOT EDIT THIS CELL
// Definfing the data schema
val customSchema = StructType(Array(StructField("answerer", IntegerType, true), StructField("questioner", IntegerType, true),
    StructField("timestamp", LongType, true)))

// COMMAND ----------

// STARTER CODE - YOU CAN LOAD ANY FILE WITH A SIMILAR SYNTAX.
// MAKE SURE THAT YOU REPLACE THE examplegraph.csv WITH THE mathoverflow.csv FILE BEFORE SUBMISSION.
val df = spark.read
   .format("com.databricks.spark.csv")
   .option("header", "false") // Use first line of all files as header
   .option("nullValue", "null")
   .schema(customSchema)
   .load("/FileStore/tables/mathoverflow.csv")
   .withColumn("date", from_unixtime($"timestamp"))
   .drop($"timestamp")

// COMMAND ----------

//display(df)
df.show()

// COMMAND ----------

// PART 1: Remove the pairs where the questioner and the answerer are the same person.
// ALL THE SUBSEQUENT OPERATIONS MUST BE PERFORMED ON THIS FILTERED DATA

// ENTER THE CODE BELOW
val filterDF = df.filter(not ($"answerer" === $"questioner" ))
  
display(filterDF)

filterDF.show()


// COMMAND ----------

// PART 2: The top-3 individuals who answered the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest out-degrees.

// ENTER THE CODE BELOW

val top3_df = filterDF.groupBy($"answerer").count().withColumnRenamed("count","questions_answered")
var sorted_result= top3_df.orderBy(desc("questions_answered"),asc("answerer")).limit(3)


sorted_result.show()


// COMMAND ----------

// PART 3: The top-3 individuals who asked the most number of questions - sorted in descending order - if tie, the one with lower node-id gets listed first : the nodes with the highest in-degree.

// ENTER THE CODE BELOW

val part3_df = filterDF.groupBy($"questioner").count().withColumnRenamed("count","questions_asked")
var sorted_result3= part3_df.orderBy(desc("questions_asked"),asc("questioner")).limit(3)


sorted_result3.show()


// COMMAND ----------

// PART 4: The top-5 most common asker-answerer pairs - sorted in descending order - if tie, the one with lower value node-id in the first column (u->v edge, u value) gets listed first.

val part4_df = filterDF.groupBy($"answerer",$"questioner").count().withColumnRenamed("count","pair_count")
var sorted_result4= part4_df.orderBy(desc("pair_count"),asc("answerer"),asc("questioner")).limit(5)


sorted_result4.show()



// COMMAND ----------

// PART 5: Number of interactions (questions asked/answered) over the months of September-2010 to December-2010 (i.e. from September 1, 2010 to December 31, 2010). List the entries by month from September to December.

// Reference: https://www.obstkel.com/blog/spark-sql-date-functions
// Read in the data and extract the month and year from the date column.
// Hint: Check how we extracted the date from the timestamp.

// ENTER THE CODE BELOW

val part5_df = filterDF.withColumn("month", month($"date")).withColumn("year", year($"date")).filter("year == 2010").groupBy("month").count().filter("month <= 12").filter("month >= 9").sort($"month")

part5_df.show()


// COMMAND ----------

// PART 6: List the top-3 individuals with the maximum overall activity, i.e. total questions asked and questions answered.

// ENTER THE CODE BELOW

val df1 = filterDF.groupBy($"answerer").count()

val df2 = filterDF.groupBy($"questioner").count()

val df3 = df1.union(df2)

val part6_df = df3.groupBy("answerer").agg(sum($"count")).withColumn("user_id", $"answerer").drop("answerer").withColumn("total_activity", $"sum(count)").drop($"sum(count)").sort($"total_activity".desc, $"user_id").limit(3)

part6_df.show()


// COMMAND ----------


