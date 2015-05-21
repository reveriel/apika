OUT = main

all: $(OUT) clean

$(OUT):  $(wildcard *.tex *.bib *.cls *.eps)
	xelatex $@
	xelatex $@
	xelatex $@

clean:
	rm -f *~ *.aux *.dvi *.bbl *.blg *.log *.out *.toc
