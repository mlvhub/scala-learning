.PHONY: clean
clean:
	rm -rf ziolambdasimple ziolambdasimple.build_artifacts.txt layer.zip

.PHONY: build-native
build-native:
	scala-cli \
		--power package \
		--main-class ziolambdasimple.LambdaHandler \
		--native-image . \
		-o ziolambdasimple \
		-- \
		--no-fallback \
		--enable-url-protocols=http 

.PHONY: package
package:
	zip layer.zip bootstrap ziolambdasimple

