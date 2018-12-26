REPO ?= kocubinski
image = $(REPO)/tuttle:latest

docker:
	docker build -t $(image) .
	docker push $(image)
