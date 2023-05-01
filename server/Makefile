IMAGE_NAME=my_capstone_image
CONTAINER_NAME=myCapstoneProjectContainer
DATA_DIR=/var/folders/data/mysql
PASSWORD=TOOR
VOLUME_NAME=Capstone_volume


check-docker:
	@which docker  || (echo "Docker not found!"; exit 1)

build:
	docker build -t $(IMAGE_NAME) .

run:
	if [ -z `docker volume ls -q -f name=$(VOLUME_NAME)` ]; then docker volume create $(VOLUME_NAME);fi
	docker run -d -p 3306:3306 --name $(IMAGE_NAME) \
 	-v $(VOLUME_NAME):/var/lib/mysql \
	 -e MYSQL_ROOT_PASSWORD=$(PASSWORD) $(IMAGE_NAME);


stop:
	docker stop $(CONTAINER_NAME);

rm:
	docker rm $(CONTAINER_NAME);

rmi:
	docker rmi $(IMAGE_NAME);

clean:
	stop rm rmi;