* Creating an image

Run the following from the directory which contains the dockerfile to create the docker image:
```sh
# Build.
$ docker build --tag example-axon/example-bug-db:SNAPSHOT .

# Build without using the cache.
$ docker build --no-cache --tag example-axon/example-bug-db:SNAPSHOT .
```
