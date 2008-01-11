
#TODO
BOOST_ROOT = ../../system/boost

RCSSBASE = ./rcss

CPPFLAGS=-I/usr/X11R6/include

rcssbase:
	cd rcss-src/rcssbase-11.1.0 && ./configure --prefix=$(RCSSBASE) --with-boost=$(BOOST_ROOT)
	cd rcss-src/rcssbase-11.1.0 && make
	cd rcss-src/rcssbase-11.1.0 && make install

rcssserver:
	cd rcss-src/rcssserver-11.1.2 && ./configure --prefix=$(RCSSBASE) --with-boost=$(BOOST_ROOT)
	cd rcss-src/rcssserver-11.1.2 && make
	cd rcss-src/rcssserver-11.1.2 && make install

rcssmonitor:
	cd rcss-src/rcssmonitor-11.1.1 && ./configure --prefix=$(RCSSBASE) --with-boost=$(BOOST_ROOT)
	cd rcss-src/rcssmonitor-11.1.1 && make
	cd rcss-src/rcssmonitor-11.1.1 && make install

tools:
	cd keepaway-0.6/tools && make
	cd keepaway-0.6/tools && cp hist killserver kunzip kwyzipper kzip monitor winsum ../../$(RCSSBASE)/bin

player:
	cd keepaway-0.6/player && make