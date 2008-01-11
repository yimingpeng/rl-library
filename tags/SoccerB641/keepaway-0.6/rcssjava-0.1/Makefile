# Makefile for RCSSJava

JAVA=java
JAVAC=javac
ANTLR_CLASSPATH=antlrall.jar
OPTS=-Xlint:unchecked

CLASSPATH=${ANTLR_CLASSPATH}
LOGDRAW_DIR=rcssjava/logplayer

.PHONY: all docs clean

all: rcssjava.jar #docs

rcssjava.jar: clean
	${JAVA} -cp ${CLASSPATH} antlr.Tool -o ${LOGDRAW_DIR} ${LOGDRAW_DIR}/LogDrawParser.g
	${JAVAC} ${OPTS} -cp ${CLASSPATH} `find rcssjava -name \*.java`
	jar c0mf META-INF/MANIFEST.MF rcssjava.jar `find pngs -name \*.png` `find rcssjava -name \*.class`

docs:
	javadoc -d docs `find rcssjava -name \*.java`

clean:
	rm -rf rcssjava.jar `find rcssjava -name \*.class` `find . -name *~` ${LOGDRAW_DIR}/LogDrawParser.java ${LOGDRAW_DIR}/LogDrawLexer.java ${LOGDRAW_DIR}/LogDrawTokenTypes.java ${LOGDRAW_DIR}/LogDrawTokenTypes.txt

