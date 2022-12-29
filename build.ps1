javac --release 19 --enable-preview ./src\ie\atu\sw\FileProcessor.java ./src\ie\atu\sw\Processor.java ./src\ie\atu\sw\Runner.java ./src\ie\atu\sw\WordDescriptions.java ./src\ie\atu\sw\WordInfo.java

cd ./src/
jar -cf ../indexer.jar *
cd ..
