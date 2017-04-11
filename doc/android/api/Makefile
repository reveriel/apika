all:
		make -C sensor/doc/ 
		find sensor/doc/ -iname "*.pdf" | xargs -J % ln -sf % .

clean:
		make -C sensor/doc/ clean-all
		rm -f *.pdf
