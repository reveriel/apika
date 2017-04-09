all:
	xelatex sensor
	xelatex wake-up-non-wake-up-sensor
	make clean

clean:
	rm -f *.aux
	rm -f *.log
	rm -f *.out

clean-pdf: clean
	rm -f *.pdf

clean-all: clean-pdf
	rm -f *~
