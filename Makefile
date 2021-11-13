# Water Flow Makefile
# Rea Keebine
# 6 September 2020

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc
OUTDIR=out

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES = Terrain.class Water.class FlowThread.class \
          TimeText.class FlowPanel.class FlowCtrl.class \
		  FlowApp.class

CLASS_FILES = $(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

run:
	java -cp bin FlowApp largesample_in.txt
	
runmed:
	java -cp bin FlowApp medsample_in.txt

test:
	./scripts/CheckThreads.sh

docs:
	javadoc -cp $(BINDIR) -d $(DOCDIR) $(SRCDIR)/*.java

cleandocs:
	rm -rf $(DOCDIR)/*