.PHONY: clean
clean:
	rm -rf ziolambdasimple ziolambdasimple.build_artifacts.txt layer.zip response.txt

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

.PHONY: delete-lambda
delete-lambda:
	aws lambda delete-function --function-name ziolambdasimple

.PHONY: create-lambda
create-lambda:
	aws lambda create-function --function-name ziolambdasimple \
		--zip-file fileb://layer.zip \
		--handler ziolambdasimple.LambdaHandler::run \
		--runtime provided.al2023 \
		--role $(role)

.PHONY: invoke
invoke:
	./scripts/invoke-lambda.sh

.PHONY: aws
aws: delete-lambda clean build-native package create-lambda invoke

