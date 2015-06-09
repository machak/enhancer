set IDEA_HOME=%ProgramFiles(x86)%\JetBrains\IntelliJ IDEA 14.0.3
set IDEA_VERSION=IDEA-IU-135.690
set IDEA_GROUP_ID=com.intellij

call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\openapi.jar" -DartifactId=openapi -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\annotations.jar" -DartifactId=annotations -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\extensions.jar" -DartifactId=extensions -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\lib\util.jar" -DartifactId=util -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\redist\forms_rt.jar" -DartifactId=forms_rt -Dversion=%IDEA_VERSION%
call mvn install:install-file -DgroupId=%IDEA_GROUP_ID% -Dpackaging=jar -Dfile="%IDEA_HOME%\redist\javac2.jar" -DartifactId=javac2 -Dversion=%IDEA_VERSION%
