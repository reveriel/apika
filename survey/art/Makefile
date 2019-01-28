OUT = main

all: $(OUT) clean

$(OUT):  $(wildcard *.tex *.bib *.cls *.eps)
	# xelatex $@
	# xelatex $@
	# xelatex $@
	latexmk -xelatex main

clean:
	rm -f *~ *.aux *.dvi *.bbl *.blg *.log *.out *.toc
