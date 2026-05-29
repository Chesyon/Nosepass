FROM gradle:jdk21

EXPOSE 8000

WORKDIR /app

RUN apt-get install python3

ADD https://bootstrap.pypa.io/get-pip.py /get-pip.py
RUN python3 /get-pip.py --break-system-packages
ADD --unpack=true https://github.com/Chesyon/sky-compass/archive/refs/heads/main.tar.gz .
RUN pip install ./sky-compass-main[api] granian --break-system-packages

COPY ./build.gradle.kts ./settings.gradle.kts ./gradle.properties ./gradlew ./gradle ./src .
RUN gradle distTar

WORKDIR /app/build/distributions
RUN tar -xf nosepass-1.0.0.tar

COPY ./launch.sh .
RUN chmod +x launch.sh

CMD ["./launch.sh"]
