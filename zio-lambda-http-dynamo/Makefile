FUNCTION_NAME=ziolambda
HANDLER=ziolambda.LambdaHandler

.PHONY: clean
clean:
	rm -rf lambda lambda.build_artifacts.txt layer.zip response.txt
	scala-cli clean .

.PHONY: build-native
build-native:
	scala-cli \
		--power package \
		--main-class $(HANDLER) \
		--native-image . \
		-o lambda \
		-- \
		--no-fallback \
		--enable-url-protocols=http \
		--initialize-at-build-time=org.slf4j.LoggerFactory \
		--verbose

.PHONY: package
package:
	zip layer.zip bootstrap lambda

.PHONY: delete-lambda
delete-lambda:
	aws lambda delete-function --function-name $(FUNCTION_NAME)

.PHONY: create-lambda
create-lambda:
	aws lambda create-function --function-name $(FUNCTION_NAME) \
		--zip-file fileb://layer.zip \
		--handler $(HANDLER)::run \
		--runtime provided.al2023 \
		--role $(role)

.PHONY: update-lambda-env-vars
update-lambda-env-vars:
	./scripts/update-lambda-env-vars.sh $(FUNCTION_NAME)

.PHONY: invoke
invoke:
	./scripts/invoke-lambda.sh $(FUNCTION_NAME)

.PHONY: aws
aws: delete-lambda clean build-native package create-lambda update-lambda-env-vars invoke

.PHONY: run
run:
	./scripts/run.sh
