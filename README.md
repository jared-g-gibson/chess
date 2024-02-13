# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Chess Server Design](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=MQAgCgFghgzgpiATAKGVAxgFwPYCcQDCANgJZwB2myADlLpieibZSAMpy4BunNdDTFpnacujOH3qNmUVgBEomKAEF06ODBjIAJoqgAjWAm37UAc1zYArtRDByULgE8QAJThmSMTLkUls5HYA7hAkmBLEZKwAtAB8ItycAFwgANpgAPJsACoAuiAA9FbwuAA65ADexZwOALZwADQgtJpBeNpNcLVQJEQAvsgcifhxCWLqKbgeXuG4ABTVuHWNzbAwbbgdIF09RACUg6LiIKMKSqrqmilmcJgAqiULJcsHZypqGjAn8SYpbACiABl-gRsiBFssQAAxVwZACy4JKyDeF0+J1GQ3GcBS5CsRCIh24x1OelRVxA6CmijgD04TxqUHqTRa63ar1JH003xAvxAAEkAHIA1xgiGMlYsjZbHa9fkC7IZRG8THE+IozkwFKUuDU5RWTAQelLcXs84a7m8wXC0XPcVNKD6iDZbAAawocoVIAdBuRHMuX2iGKOEy9judbvIhKx6PimOSoYN4YoUc4McIpAomBSiAADDnyhVvU7XRQBhRtOZLDY7HhZDcQIDsJ5AsAQmEIhmYrHRPH0lk8oV4Jp-OQC2KmatWu0BnGRt2iSGiE2SOQjctmWspQcVepuer-ddbrT5uO4Kb3v6Lfo-kCQTaGfUveRtJPWZtobCEYtfWbL4H51iKTfjuCAkr+nxalS4R6gaa4mj+F5oqMlpCv8IpKsaE5FjgEYeoqRYIWSAZBgu2IgFUtpYWGJbkDOwagSRPC4CkFEPisRZJrRKYjKMkSZtmeZjpR7HURGZbPpW1i2MAFhwO6jZmNYwitqE4TIHxXZjL2ci3tk-z5AUQ4wCO5QcTR3HciBKTaHARC3HAcxmRG270XufoQTytn2TBhpORQ55EVeKQ6cCekJsWuEwvC4WEea-5jOISRRsSjHJBZ8UaVmSB5qg5aSdWwBTC+gIzCAADi4pfCp7bqZ2wipcxaRlf8A4FGY4qmaJyazt8VkgDcmAVfUMCOV15AuaRbngeSA0+aNiY0QFcU-Ne7C3qC4WcSAABmli1DF+5IXEfW4viyW7nEh3kqQ3hDRocxLX+K03sCG3tcNO17f14qxX+jGJeRIAAETvRoQMpKkFQg+KfJyODIAAIyIAAzAALE05RA224THss4NA0DTRA-oRAYC6uPivjhMgJjoMCuKSQE30IC5HRk3xbOLHA6DMDw5D0P1LD8NI2jGPkFjqk0sJVNEyTZMU-UMs0+LdMM0zLNs0x3yZQJ+aVNzlV81DoNCykIvo8rEvtgrcBK8TpPoOT0uM9TtPivT9Qu8zrO5RJyAWFJdjUFYuDUHZhBQQgd3BJLtVRPVAG9pkOQGaDnULRGBaq-Umupv9IbatSczZyJGf+edoFqu5M23Hdxfu-BV0Bs9a2vWCJcftFoO-UdAEA6dRAgBXU2IeShfhHXJePUhLdWmh7cNxOJtyGL2NS2xTRy47NsNOUoN4d99Q91yHP0Sxy+aylieNRUF-pbxdXnzDcgDL7Fb+1W0lOLZS5BCAABS2AVzlXFHYLeLo46Zksj2Rq6Q7itTTg4MaBZMoEGwEuXATQ749WOmfEAAArIBq4-LkCaBUVB6C8BYOfn0Ca0ZLrV01P1W4c0SHTxPi3AEbdNo0U7giAiTcYwnTxASEC3wm5AWoLoCe4o5jkLqoQShmDD5wFhrQ4+zceSrTnuhChGCQArhwOVZQcJ-gaLTH1Ye-5Ob31iDrbKOZkB5Q-oHGSvgXDEB1PgKA1Aw6MD8AEGONVMrQOGBDEKLV9KFBMOlPuBc7J0AelY2IEiKQJPmOwzRvIIlhQEYwoKIAcn-AOvk5Cq0ikYXMdYvBA8rENRSLY9M8ddZOIkkAA)

Here is an overview of the chess server.
