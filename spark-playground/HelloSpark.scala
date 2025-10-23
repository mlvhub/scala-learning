// NOTE: taken and adapted from https://medium.com/@lav.chaudhary5/mastering-apache-spark-with-scala-from-basics-to-advanced-analytics-9b5a3eccddb8

//> using scala 3.7.3

//> using dep org.apache.spark:spark-core_2.13:4.0.1
//> using dep org.apache.spark:spark-sql_2.13:4.0.1

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

object HelloSpark:
    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder
        .appName("Simple Application")
        .master("local[*]")
        .getOrCreate()

        import spark.implicits.*

        // Creating the first DataFrame
        val data1 = Seq(
            Seq("John", "Sales", 3000),
            Seq("Doe", "Sales", 4600)
        ).map(Row.fromSeq)
        val schema1 = StructType(Seq(
            StructField("name", StringType, true),
            StructField("department", StringType, true),
            StructField("salary", IntegerType, true)
        ))
        val dataFrame1 = spark.createDataFrame(spark.sparkContext.parallelize(data1), schema1)

        // Creating the second DataFrame
        val data2 = Seq(
            Seq("Sales", "New York"),
            Seq("Engineering", "San Francisco")
        ).map(Row.fromSeq)
        val schema2 = StructType(Seq(
            StructField("department", StringType, true),
            StructField("location", StringType, true)
        ))
        val dataFrame2 = spark.createDataFrame(spark.sparkContext.parallelize(data2), schema2)

        // Joining the DataFrames on the "department" column
        val joinedDF = dataFrame1.join(dataFrame2, "department")

        // Performing an aggregation: average salary by department
        val aggregatedDF = joinedDF.groupBy("department").agg(avg("salary").as("avg_salary"))

        // Printing the results
        aggregatedDF.show()

        // Stop the SparkSession
        spark.stop()
    }
