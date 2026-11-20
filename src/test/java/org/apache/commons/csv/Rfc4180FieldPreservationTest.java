/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Rfc4180FieldPreservationTest {

    static Stream<Arguments> adjacentEmptyFields() {
        return Stream.of(
                Arguments.of("quoted comma before empty middle field",
                        "\"Live, 1977\",,5", Arrays.asList("Live, 1977", "", "5")),
                Arguments.of("empty first field before doubled quote",
                        ",\"Side \"\"A\"\"\",5", Arrays.asList("", "Side \"A\"", "5")),
                Arguments.of("multiline field before empty final field at EOF",
                        "5,\"Side A\r\nSide B\",", Arrays.asList("5", "Side A\r\nSide B", "")),
                Arguments.of("multiline field before empty final field at CRLF",
                        "5,\"Side A\r\nSide B\",\r\n", Arrays.asList("5", "Side A\r\nSide B", "")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("adjacentEmptyFields")
    void preservesFields(final String scenario, final String input, final List<String> expected) throws IOException {
        try (CSVParser parser = CSVParser.parse(input, CSVFormat.RFC4180)) {
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(1, records.size(), scenario);
            assertEquals(expected, records.get(0).toList(), scenario);
        }
    }
}
