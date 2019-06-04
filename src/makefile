client.jar:
	javac server/*.java
	javac client/*.java
	jar cvf client.jar client/*.class

run6: client.jar
	@echo '### Executing 6 nodes app...'
	./run.sh ../resources/nodes6edges12.csv

run10: client.jar
	@echo '### Executing 12 nodes app...'
	./run.sh ../resources/nodes10edges20.csv

run20: client.jar
	@echo '### Executing 20 nodes app...'
	./run.sh ../resources/nodes20edges50.csv

run50: client.jar
	@echo '### Executing 50 nodes app...'
	./run.sh ../resources/nodes50edges100.csv

clean:  # remove class and library files
	@echo 'clearing files...'
	@rm -f */*.class *.so *.jar *.class
	@ls *