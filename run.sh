rm result.txt
touch result.txt
rm chaoh/*.class
javac chaoh/*.java
jar cmvf META-INF/MANIFEST.MF run.jar chaoh/ 
java -jar run.jar
