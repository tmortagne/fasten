/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.fasten.analyzer.graphplugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "GraphPlugin")
public class Main implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @CommandLine.Option(names = {"-f", "--file"},
            paramLabel = "JSON",
            description = "Path to JSON file which contains edges")
    String jsonFile;

    @CommandLine.Option(names = {"-kb", "--kbDirectory"},
            paramLabel = "kbDir",
            description = "The directory of the RocksDB instance containing the knowledge base")
    String kbDir;

    @CommandLine.Option(names = {"-kbmeta", "--kbMetadataFile"},
            paramLabel = "kbMeta",
            description = "The file containing the knowledge base metadata")
    String kbMeta;

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        var graphPlugin = new GraphDatabasePlugin.GraphDBExtension();
        try {
            graphPlugin.setKnowledgeBase(kbDir, kbMeta);
        } catch (RocksDBException | IOException | ClassNotFoundException e) {
            e.printStackTrace(System.err);
            return;
        }
        final String fileContents;
        try {
            fileContents = Files.readString(Paths.get(jsonFile));
        } catch (IOException e) {
            logger.error("Could not find the JSON file at " + jsonFile, e);
            return;
        }
        String artifact = fileContents.split("\n")[0];
        String graph = fileContents.split("\n")[1];
        final var record = new ConsumerRecord<>("fasten.cg.edges", 0, 0L, artifact, graph);
        graphPlugin.consume("fasten.cg.edges", record);
    }
}
