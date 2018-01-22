#!/bin/sh
. ~/.bashrc
cd /spark/app/bin
JAR_PATH=../lib/opentsp-location-parallel-1.208-RC47-SNAPSHOT.jar

for jar in `ls ../lib/*.jar`
do
  if [ $jar != $JAR_PATH ] ; then
    LIBJARS=$jar,$LIBJARS
  fi
done

day=$(date -d "1 days ago" +%Y%m%d)
spark-submit --class com.navinfo.opentsp.platform.computing.parallel.application.SaveGpsDataApplicaton --executor-memory 2G --driver-memory 2G --supervise --conf spark.eventLog.enabled=true  --total-executor-cores 20 --conf spark.executor.cores=1  --conf spark.executor.extraJavaOptions="-XX:+UseCompressedOops -XX:-UseGCOverheadLimit" --conf spark.default.parallelism=300 --deploy-mode client --jars  $LIBJARS  $JAR_PATH $day
#export CLASSPATH=$CLASSPATH:../config/

#spark-submit --jars $LIBJARS --class com.mapbar.didi.DiDiAnalysisAppForETA --master local[2] --deploy-mode client $JAR_PATH $1 $2 $3 $4
#spark-submit --class com.mapbar.didi.DiDiAnalysisApp1 --master spark://10.30.20.25:7077 --executor-memory 4G --total-executor-cores 4 client didi-analysis-1.0.jar $1 $2 $3 $4
