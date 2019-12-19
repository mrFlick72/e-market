FROM openjdk:11

ADD product-catalog.jar /usr/local/product-catalog/

VOLUME /var/log/e-market

WORKDIR /usr/local/product-catalog/

CMD ["java",  "-jar", "product-catalog.jar"]