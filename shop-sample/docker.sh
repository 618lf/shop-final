#! /bin/bash

BUILD_DIR="/var/lib/docker/tmp/build-$RANDOM"

mkdir "${BUILD_DIR}"

cp target/shop-sample-RELEASE.zip "${BUILD_DIR}"

cd "${BUILD_DIR}"

unzip shop-sample-RELEASE.zip

cd shop-sample

docker build -t shop-sample/latest .

rm -rf "${BUILD_DIR}"