
JAVACC=javacc
JAVAc=javac

all: ExprEval.class SimpleC.class 

ExprEval.class: ExprEval.java
	$(JAVAC) ExprEval.java

SimpleC.class: SimpleC.jj
	$(JAVACC) SimpleC.jj 
	javac SimpleC.java

clean:
	rm SimpleC.java SimpleCConstants.java SimpleCharStream.java TokenMgrError.java ParseException.java SimpleCTokenManager.java Token.java *.class
