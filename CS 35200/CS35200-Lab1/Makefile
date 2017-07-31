
JAVACC=javacc
JAVAC=javac

all: DecimalParser.class FloatParser.class SimpleC.class

DecimalParser.class: DecimalParser.java
	$(JAVAC) DecimalParser.java

FloatParser.class: FloatParser.java
	$(JAVAC) FloatParser.java

SimpleC.class: SimpleC.jj
	$(JAVACC) SimpleC.jj 
	javac SimpleC.java

clean:
	rm SimpleC.java SimpleCConstants.java SimpleCharStream.java TokenMgrError.java ParseException.java SimpleCTokenManager.java Token.java *.class
