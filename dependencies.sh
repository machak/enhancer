echo "installing dependencies"
IDEA_HOME=/home/machak/idea11
IDEA_VERSION=IDEA-IU-111.63
IDEA_GROUP_ID=com.intellij

mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/lib/openapi.jar" -DartifactId=openapi -Dversion=${IDEA_VERSION}
mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/lib/annotations.jar" -DartifactId=annotations -Dversion=${IDEA_VERSION}
mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/lib/extensions.jar" -DartifactId=extensions -Dversion=${IDEA_VERSION}
mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/lib/util.jar" -DartifactId=util -Dversion=${IDEA_VERSION}
mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/redist/forms_rt.jar" -DartifactId=forms_rt -Dversion=${IDEA_VERSION}
mvn install:install-file -DgroupId=${IDEA_GROUP_ID} -Dpackaging=jar -Dfile="${IDEA_HOME}/redist/javac2.jar" -DartifactId=javac2 -Dversion=${IDEA_VERSION}
