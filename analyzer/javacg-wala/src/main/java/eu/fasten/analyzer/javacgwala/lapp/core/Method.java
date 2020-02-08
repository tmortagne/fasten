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


package eu.fasten.analyzer.javacgwala.lapp.core;

import com.ibm.wala.types.Selector;
import eu.fasten.core.data.FastenJavaURI;
import eu.fasten.core.data.FastenURI;

import java.util.HashMap;
import java.util.Map;

public abstract class Method {

    public final String namespace;
    public final Selector symbol;
    public final Map<String, String> metadata;


    protected Method(String namespace, Selector symbol) {
        this.namespace = namespace;
        this.symbol = symbol;
        this.metadata = new HashMap<>();
    }

    /**
     * Convert {@link Method} to ID representation.
     *
     * @return - method ID
     */
    public abstract String toID();

    /**
     * Convert {@link FastenJavaURI} to {@link FastenURI}.
     *
     * @param javaURI - FastenJavaURI to convert
     * @return - {@link FastenURI}
     */
    public static FastenURI toCanonicalSchemalessURI(FastenJavaURI javaURI) {

        return FastenURI.createSchemeless(javaURI.getRawForge(), javaURI.getRawProduct(),
                javaURI.getRawVersion(),
                javaURI.getRawNamespace(), javaURI.getRawEntity());
    }

}
